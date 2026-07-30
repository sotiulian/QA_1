package com.wwp.QA.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.wwp.QA.R;
import com.wwp.QA.RoomDatabase.DatabaseClient;
import com.wwp.QA.RoomDatabase.SysadminEntity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Portat din pms_3u354/app/.../utils/AppUpdater.java (aceeasi structura ca mm/AppUpdater.java).
 *
 * Adresa serverului vine din baza de date Room (SysadminEntity.getWebaddress() via DatabaseClient),
 * aceeasi sursa folosita de QARetrofitService pentru toate apelurile API - citire asincrona
 * (ExecutorService+Handler).
 *
 * Cale server-side: /pms/update/qa-version.json, pe acelasi server ca PMS si mm, dar cu fisier
 * distinct (nu se suprapune cu version.json al PMS-ului sau mm-version.json al mm-ului).
 */
public class AppUpdater {

    private static final String TAG = "AppUpdater";
    private static final String VERSION_JSON_PATH = "/pms/update/qa-version.json";
    private static final String APK_FILE_NAME = "qa-update.apk";

    public static void checkForUpdate(Activity activity) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            SysadminEntity sysadminEntity = DatabaseClient
                    .getInstance(activity.getApplicationContext())
                    .getAppDatabase()
                    .sysadminDao()
                    .getActivewebadress();

            if (sysadminEntity == null || sysadminEntity.getWebaddress() == null || sysadminEntity.getWebaddress().isEmpty()) {
                return;
            }

            String versionUrl = "http://" + sysadminEntity.getWebaddress() + VERSION_JSON_PATH;

            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(versionUrl).openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.connect();

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    conn.disconnect();
                    return;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();
                conn.disconnect();

                JSONObject json = new JSONObject(sb.toString());
                int serverVersionCode = json.getInt("versionCode");
                String serverVersionName = json.getString("versionName");
                String apkUrl = json.getString("apkUrl");

                if (serverVersionCode > getCurrentVersionCode(activity)) {
                    handler.post(() -> showUpdateDialog(activity, serverVersionName, apkUrl));
                }

            } catch (Exception e) {
                Log.w(TAG, "Update check failed: " + e.getMessage());
            }
        });
    }

    @SuppressWarnings("deprecation")
    private static int getCurrentVersionCode(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager()
                    .getPackageInfo(activity.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return (int) info.getLongVersionCode();
            }
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    private static void showUpdateDialog(Activity activity, String versionName, String apkUrl) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.update_available_title))
                .setMessage(activity.getString(R.string.update_available_message, versionName))
                .setPositiveButton(activity.getString(R.string.update_btn_update), (d, w) -> {
                    if (!canInstallPackages(activity)) {
                        requestInstallPermission(activity);
                        return;
                    }
                    downloadAndInstall(activity, versionName, apkUrl);
                })
                .setNegativeButton(activity.getString(R.string.update_btn_later), null)
                .setCancelable(false)
                .show();
    }

    private static boolean canInstallPackages(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return activity.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    private static void requestInstallPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                    .setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivity(intent);
            Toast.makeText(activity,
                    activity.getString(R.string.update_install_permission),
                    Toast.LENGTH_LONG).show();
        }
    }

    private static void downloadAndInstall(Activity activity, String versionName, String apkUrl) {
        AlertDialog progressDialog = new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.update_downloading_title, versionName))
                .setMessage(activity.getString(R.string.update_downloading_message))
                .setCancelable(false)
                .create();
        progressDialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                File outputFile = new File(activity.getCacheDir(), APK_FILE_NAME);

                HttpURLConnection conn = (HttpURLConnection) new URL(apkUrl).openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(60000);
                conn.connect();

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    conn.disconnect();
                    throw new Exception("Server a returnat " + conn.getResponseCode() + " pentru APK URL");
                }

                InputStream input = conn.getInputStream();
                FileOutputStream output = new FileOutputStream(outputFile);
                byte[] buffer = new byte[8192];
                int count;
                while ((count = input.read(buffer)) != -1) {
                    output.write(buffer, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                conn.disconnect();

                final File finalFile = outputFile;
                handler.post(() -> {
                    progressDialog.dismiss();
                    installApk(activity, finalFile);
                });

            } catch (Exception e) {
                Log.e(TAG, "Download failed: " + e.getMessage());
                handler.post(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity,
                            activity.getString(R.string.update_download_error, e.getMessage()),
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private static void installApk(Activity activity, File apkFile) {
        Uri apkUri = FileProvider.getUriForFile(
                activity,
                activity.getApplicationContext().getPackageName() + ".fileprovider",
                apkFile
        );

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
