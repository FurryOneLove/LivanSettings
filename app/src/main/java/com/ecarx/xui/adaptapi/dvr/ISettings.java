package com.ecarx.xui.adaptapi.dvr;

import com.ecarx.xui.adaptapi.Tribool;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface ISettings {
    public static final int CRASH_SENSITIVITY_LEVEL_DEFAULT = 2147483637;
    public static final int CRASH_SENSITIVITY_LEVEL_HIGH = 3;
    public static final int CRASH_SENSITIVITY_LEVEL_LOW = 1;
    public static final int CRASH_SENSITIVITY_LEVEL_MIDDLE = 2;
    public static final int CRASH_SENSITIVITY_LEVEL_OFF = 0;
    public static final int RESOLUTION_1080P_30FPS = 2;
    public static final int RESOLUTION_720P_30FPS = 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CrashSensitivityLevel {
    }

    public interface IDvrSettingsCallback {
        void onCrashSensitivityLevelChanged(int i);

        void onFactoryResetStatus(int i);

        void onParkMonitorChange(boolean z);

        void onRecordAudioCfg(boolean z);

        void onRecordingDurationChange(int i);

        void onResolutionTypeChange(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ResolutionType {
    }

    boolean factoryReset();

    int getRecordingDuration();

    int getResolutionType();

    int[] getSupportedRecordingDuration();

    Tribool ifRecordAudio();

    void setAudioRecordingCfg(boolean z);

    void setCallback(IDvrSettingsCallback iDvrSettingsCallback);

    boolean setCrashSensitivityLevel(int i);

    boolean setParkMonitor(boolean z);

    void setRecordingDuration(int i);

    void setResolutionType(int i);

    void unsetCallback(IDvrSettingsCallback iDvrSettingsCallback);
}
