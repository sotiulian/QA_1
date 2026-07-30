package com.wwp.QA;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wwp.QA.Utils.QrAutoFocusHelper;

/**
 * Inlocuieste com.blikoon.qrcodescanner.QrCodeActivity (modul :qrcodescanner, Camera1/ZXing).
 * Portat din pms_3u354/mm, pastrand acelasi extra de rezultat
 * ("com.blikoon.qrcodescanner.got_qr_scan_relult") ca sa nu fie nevoie sa se schimbe
 * onActivityResult/ActivityResultCallback din activitatile apelante.
 */
public class QrScanActivity extends AppCompatActivity {

    public static final String EXTRA_RESULT = "com.blikoon.qrcodescanner.got_qr_scan_relult";
    public static final String EXTRA_ERROR  = "com.blikoon.qrcodescanner.error_decoding_image";

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final long VIBRATE_MS = 200L;

    private PreviewView previewView;
    private ImageButton btnFlash;
    private Camera camera;
    private ExecutorService cameraExecutor;
    private volatile boolean resultSent = false;
    private boolean torchOn = false;
    private QrAutoFocusHelper autoFocusHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        previewView = findViewById(R.id.preview_view);
        btnFlash    = findViewById(R.id.btn_flash);
        btnFlash.setOnClickListener(v -> toggleTorch());

        cameraExecutor = Executors.newSingleThreadExecutor();
        autoFocusHelper = new QrAutoFocusHelper();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @SuppressWarnings("UnsafeOptInUsageError")
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> future = ProcessCameraProvider.getInstance(this);
        future.addListener(() -> {
            try {
                ProcessCameraProvider provider = future.get();

                Preview.Builder previewBuilder = new Preview.Builder();
                autoFocusHelper.applyCamera2Options(previewBuilder);
                Preview preview = previewBuilder.build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                BarcodeScanner scanner = BarcodeScanning.getClient(
                        new BarcodeScannerOptions.Builder()
                                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                                .build());

                ImageAnalysis analysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                analysis.setAnalyzer(cameraExecutor, imageProxy -> {
                    if (resultSent) {
                        imageProxy.close();
                        return;
                    }
                    Image mediaImage = imageProxy.getImage();
                    if (mediaImage == null) {
                        imageProxy.close();
                        return;
                    }
                    InputImage image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.getImageInfo().getRotationDegrees());

                    scanner.process(image)
                            .addOnSuccessListener(barcodes -> {
                                for (Barcode barcode : barcodes) {
                                    String value = barcode.getRawValue();
                                    if (value != null && !value.isEmpty() && !resultSent) {
                                        resultSent = true;
                                        vibrate();
                                        Intent data = new Intent();
                                        data.putExtra(EXTRA_RESULT, value);
                                        setResult(Activity.RESULT_OK, data);
                                        finish();
                                        return;
                                    }
                                }
                            })
                            .addOnCompleteListener(task -> imageProxy.close());
                });

                camera = provider.bindToLifecycle(
                        this, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis);
                autoFocusHelper.start(camera);

            } catch (ExecutionException | InterruptedException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Eroare camera: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void toggleTorch() {
        if (camera != null && camera.getCameraInfo().hasFlashUnit()) {
            torchOn = !torchOn;
            camera.getCameraControl().enableTorch(torchOn);
            btnFlash.setImageResource(torchOn
                    ? android.R.drawable.ic_menu_close_clear_cancel
                    : android.R.drawable.ic_menu_camera);
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (v == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(VIBRATE_MS, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //noinspection deprecation
            v.vibrate(VIBRATE_MS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int req, @NonNull String[] perms,
                                           @NonNull int[] grants) {
        super.onRequestPermissionsResult(req, perms, grants);
        if (req == CAMERA_PERMISSION_CODE) {
            if (grants.length > 0 && grants[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Permisiunea camera este necesara pentru scanare.",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
        if (autoFocusHelper != null) autoFocusHelper.stop();
    }
}
