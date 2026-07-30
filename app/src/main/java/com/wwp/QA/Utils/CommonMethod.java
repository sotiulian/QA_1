package com.wwp.QA.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import androidx.appcompat.app.AlertDialog;

public class CommonMethod {

        public static final String DISPLAY_MESSAGE_ACTION =
                "com.codecube.broking.gcm";

        public static final String EXTRA_MESSAGE = "message";

        public  static boolean isNetworkAvailable(Context ctx) {

            ConnectivityManager connectivityManager
                    = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager == null) return false;

            Network network = connectivityManager.getActiveNetwork();
            if (network == null) return false;

            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);

            return capabilities != null
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }

        public static void showAlert(String message, Activity context) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            try {
                builder.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

