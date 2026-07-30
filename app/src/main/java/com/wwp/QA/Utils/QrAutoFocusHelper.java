package com.wwp.QA.Utils;

import android.hardware.camera2.CaptureRequest;
import android.util.Log;

import androidx.camera.camera2.interop.Camera2Interop;
import androidx.camera.core.Camera;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceOrientedMeteringPointFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Autofocus mai rapid pentru scanarea codurilor QR, folosit de QrScanActivity (full-screen).
 * Portat neschimbat din pms_3u354/mm (utils/QrAutoFocusHelper.java).
 *
 * Strategie:
 * 1. Antibanding 50Hz setat explicit, pentru flicker de la iluminatul fluorescent.
 * 2. Trigger explicit + repetat de startFocusAndMetering() pe centrul cadrului, la fiecare ~1.8s,
 *    in loc sa se bazeze exclusiv pe AF continuu implicit, care poate porni lent (hunting) mai
 *    ales pentru coduri QR scanate de aproape.
 */
public class QrAutoFocusHelper {

    private static final String TAG = "QrAutoFocusHelper";

    private static final long CONTINUOUS_TRIGGER_INTERVAL_MS = 1800;
    private static final long BURST_WINDOW_MS = 900;

    private Camera camera;
    private volatile long lastFocusActionMs = 0L;

    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> triggerFuture;

    /** De apelat pe Preview.Builder inainte de build(), ca sa prinda antibanding-ul. */
    @SuppressWarnings("UnsafeOptInUsageError")
    public void applyCamera2Options(Preview.Builder builder) {
        try {
            Camera2Interop.Extender<Preview> ext = new Camera2Interop.Extender<>(builder);
            ext.setCaptureRequestOption(CaptureRequest.CONTROL_AE_ANTIBANDING_MODE,
                    CaptureRequest.CONTROL_AE_ANTIBANDING_MODE_50HZ);
        } catch (Exception e) {
            Log.w(TAG, "Antibanding indisponibil: " + e.getMessage());
        }
    }

    /** De apelat imediat dupa provider.bindToLifecycle(), cu obiectul Camera returnat. */
    public void start(Camera camera) {
        this.camera = camera;

        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
        }

        triggerContinuousFocus();

        if (triggerFuture != null) triggerFuture.cancel(true);
        triggerFuture = scheduler.scheduleWithFixedDelay(this::triggerContinuousFocus,
                CONTINUOUS_TRIGGER_INTERVAL_MS, CONTINUOUS_TRIGGER_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    /** True daca suntem la scurt timp dupa un trigger de focus - fereastra in care merita analizat mai des. */
    public boolean isInBurstWindow() {
        return System.currentTimeMillis() - lastFocusActionMs < BURST_WINDOW_MS;
    }

    public void stop() {
        if (triggerFuture != null) {
            triggerFuture.cancel(true);
            triggerFuture = null;
        }
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
        camera = null;
    }

    private void triggerContinuousFocus() {
        if (camera == null) return;
        try {
            SurfaceOrientedMeteringPointFactory factory = new SurfaceOrientedMeteringPointFactory(1f, 1f);
            MeteringPoint point = factory.createPoint(0.5f, 0.5f);
            FocusMeteringAction action = new FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                    .disableAutoCancel()
                    .build();
            camera.getCameraControl().startFocusAndMetering(action);
            lastFocusActionMs = System.currentTimeMillis();
        } catch (Exception e) {
            Log.w(TAG, "startFocusAndMetering esuat: " + e.getMessage());
        }
    }
}
