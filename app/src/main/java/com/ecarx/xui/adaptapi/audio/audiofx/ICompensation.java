package com.ecarx.xui.adaptapi.audio.audiofx;

import com.ecarx.xui.adaptapi.FunctionStatus;
import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface ICompensation {
    public static final int AUDIO_SETTING_COMPENSATION_LEVEL = 3;

    @VendorDefinition(author = "@ECARX", date = "2021-06-23", project = "3BE02")
    public static final int AUDIO_SETTING_DTS_SOUND = 5;
    public static final int AUDIO_SETTING_EFFECT_ENHANCE = 4;
    public static final int AUDIO_SETTING_LOUDNESS_COMPENSATION = 1;
    public static final int AUDIO_SETTING_SPEED_COMPENSATION = 2;
    public static final int COMPENSATION_LEVEL_HIGH = 101;
    public static final int COMPENSATION_LEVEL_LOW = 99;
    public static final int COMPENSATION_LEVEL_MEDIUM = 100;
    public static final int DTS_MODE_CINEMA = 4;
    public static final int DTS_MODE_DYNAMIC = 2;
    public static final int DTS_MODE_NATURAL = 1;
    public static final int DTS_MODE_OFF = 0;
    public static final int DTS_MODE_VOICE = 3;
    public static final int EFFECT_ENHANCE_ALL_BLANCE = 4;
    public static final int EFFECT_ENHANCE_CENTERPOINT = 5;
    public static final int EFFECT_ENHANCE_DRIVE = 1;
    public static final int EFFECT_ENHANCE_OFF = 0;
    public static final int EFFECT_ENHANCE_PASSENGER = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CompensationLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CompensationSettings {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-06-23", project = "3BE02")
    @Retention(RetentionPolicy.SOURCE)
    public @interface DtsSoundMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EffectEnhanceMode {
    }

    public interface ICompensationSettingListener {
        void onCompensationSettingStateChanged(int i);
    }

    int getCompensationLevelOfSpeedCompensatedVolume();

    @VendorDefinition(author = "@ECARX", date = "2021-06-23", project = "3BE02")
    int getDtsSoundMode();

    int getEffectEnhanceMode();

    int[] getSupportedEffectEnhanceMode();

    FunctionStatus isCompensationSettingSupported(int i);

    @VendorDefinition(author = "@ECARX", date = "2021-06-23", project = "3BE02")
    boolean isDtsSoundEnabled();

    boolean isLoudnessEnabled();

    boolean isSpeedCompensatedVolumeEnabled();

    boolean registerCompensationSettingListener(ICompensationSettingListener iCompensationSettingListener);

    void setCompensationLevelOfSpeedCompensatedVolume(int i);

    @VendorDefinition(author = "@ECARX", date = "2021-06-23", project = "3BE02")
    boolean setDtsSoundMode(int i);

    boolean setEffectEnhanceMode(int i);

    void setLoudnessEnable(boolean z);

    void setSpeedCompensatedVolumeEnable(boolean z);

    boolean unregisterCompensationSettingListener(ICompensationSettingListener iCompensationSettingListener);
}
