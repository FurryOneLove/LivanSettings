package android.car.diagnostic;

import android.car.CarManagerBase;

/* loaded from: classes.dex */
public class CarDiagnosticManager implements CarManagerBase {
    public static final int[] FRAME_TYPES = {0, 1};
    public static final int FRAME_TYPE_FREEZE = 1;
    public static final int FRAME_TYPE_LIVE = 0;

    public interface OnDiagnosticEventListener {
        void onDiagnosticEvent(CarDiagnosticEvent carDiagnosticEvent);
    }

    @Override // android.car.CarManagerBase
    public void onCarDisconnected() {
    }

    public boolean isLiveFrameSupported() {
        throw new RuntimeException("Stub!");
    }

    public boolean registerListener(OnDiagnosticEventListener onDiagnosticEventListener, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterListener(OnDiagnosticEventListener onDiagnosticEventListener) {
        throw new RuntimeException("Stub!");
    }

    public CarDiagnosticEvent getLatestLiveFrame() {
        throw new RuntimeException("Stub!");
    }
}
