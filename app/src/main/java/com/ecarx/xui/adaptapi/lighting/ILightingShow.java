package com.ecarx.xui.adaptapi.lighting;

import com.ecarx.xui.adaptapi.FunctionStatus;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface ILightingShow {
    public static final int ERROR_CODE_ACC_NOT_CONVENIENCE = 1;
    public static final int ERROR_CODE_BATTERY_LOW = 4;
    public static final int ERROR_CODE_BRAKE_PRESSED = 6;
    public static final int ERROR_CODE_DOOR_NOT_CLOSED = 2;
    public static final int ERROR_CODE_HWL_ON = 7;
    public static final int ERROR_CODE_LAMP_FAULT = 5;
    public static final int ERROR_CODE_NO_ERROR = 0;
    public static final int ERROR_CODE_UNKNOWN = 255;
    public static final int ERROR_CODE_WINDOW_NOT_DOWN = 3;
    public static final int SHOW_MODE_MULTIPLE = 2;
    public static final int SHOW_MODE_SINGLE = 1;
    public static final int SHOW_STATE_END = 4;
    public static final int SHOW_STATE_ERROR = 5;
    public static final int SHOW_STATE_IDLE = 255;
    public static final int SHOW_STATE_PREPARE = 6;
    public static final int SHOW_STATE_SHOWING = 2;
    public static final int SHOW_STATE_START = 1;
    public static final int SHOW_STATE_STOP = 3;
    public static final int SHOW_STATE_UNKNOWN = 0;
    public static final int SHOW_TYPE_EXTERIOR = 1;
    public static final int SHOW_TYPE_INTERIOR = 2;
    public static final int VEHICLE_IP_INTERNET_MASTER = 1;
    public static final int VEHICLE_IP_LOCAL_MASTER = 2;
    public static final int VEHICLE_IP_LOCAL_SLAVE = 3;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorCode {
    }

    public interface ILightingShowWatcher {
        void onLightingShowEnableChanged(FunctionStatus functionStatus);

        void onLightingShowError(int[] iArr);

        void onLightingShowStateChanged(int i);
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShowMode {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShowState {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShowType {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface VehicleIpType {
    }

    int[] getLightingShowError();

    int getLightingShowMode();

    int getLightingShowState();

    int getLightingShowType();

    int[] getVehicleIpTable(int i);

    FunctionStatus isLightingShowEnabled();

    void registerLightingShowWatcher(ILightingShowWatcher iLightingShowWatcher);

    boolean sendIpMessage(int i, String str);

    boolean sendLightingShowSource(byte[] bArr);

    boolean sendLightingShowSourceEnd();

    boolean sendLightingShowSourceStart();

    boolean setLightingShowMode(int i);

    boolean setLightingShowType(int i);

    void startLightingShow();

    void stopLightingShow();

    void unregisterLightingShowWatcher(ILightingShowWatcher iLightingShowWatcher);
}
