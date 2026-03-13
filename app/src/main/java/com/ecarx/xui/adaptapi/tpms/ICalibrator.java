package com.ecarx.xui.adaptapi.tpms;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface ICalibrator {
    public static final int CALIBRATE_WARNING_CMN_WARN = 1;
    public static final int CALIBRATE_WARNING_FL_WARN = 2;
    public static final int CALIBRATE_WARNING_FR_WARN = 3;
    public static final int CALIBRATE_WARNING_NO_WARN = 0;
    public static final int CALIBRATE_WARNING_RL_WARN = 4;
    public static final int CALIBRATE_WARNING_RR_WARN = 5;
    public static final int CALIBRATE_WARNING_SYS_FAILR = 7;
    public static final int CALIBRATE_WARNING_SYS_NOT_AVI = 6;
    public static final int STATE_CALIBRATING = 5;
    public static final int STATE_FAILED = 3;
    public static final int STATE_IDLE = 1;
    public static final int STATE_SUCCESS = 2;
    public static final int STATE_TIME_OUT = 4;
    public static final int STATE_UNKNOWN = 2147483637;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CalibrateWarning {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CalibrationState {
    }

    public interface ICalibrationStateListener {
        void onCalibrateWarning(int i);

        void onCalibrationReady(boolean z);
    }

    public interface ITirePressureCalibrationCallback {
        void onTirePressureCalibrationStateChanged(int i);
    }

    int getCalibrateWarning();

    boolean isTirePressureCalibrationReady();

    boolean registerCalibrationStateListener(ICalibrationStateListener iCalibrationStateListener);

    boolean releaseTirePressureCalibrationCallback(ITirePressureCalibrationCallback iTirePressureCalibrationCallback);

    boolean requestTirePressureCalibration(ITirePressureCalibrationCallback iTirePressureCalibrationCallback);

    boolean unregisterCalibrationStateListener(ICalibrationStateListener iCalibrationStateListener);
}
