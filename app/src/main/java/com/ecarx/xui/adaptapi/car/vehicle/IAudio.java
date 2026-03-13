package com.ecarx.xui.adaptapi.car.vehicle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IAudio {
    public static final int ACOUSTIC_EFFECT_MODE_JAZZ = 771817729;
    public static final int ACOUSTIC_EFFECT_MODE_OFF = 0;
    public static final int ACOUSTIC_EFFECT_MODE_OPERA = 771817730;
    public static final int ACOUSTIC_EFFECT_MODE_THEATRE = 771817731;
    public static final int ECALL_MUTE_STATUS = 772079872;
    public static final int MUSIC_RHYTHM_STATUS = 772014336;
    public static final int SETTING_FUNC_ACOUSTIC_EFFECT_MODE = 771817728;
    public static final int SETTING_FUNC_AUDIO_SEPARATED = 771948800;
    public static final int SETTING_FUNC_CAE_SWITCH = 771818240;
    public static final int SETTING_FUNC_HXT_SWITCH = 771817984;
    public static final int SETTING_FUNC_MULTIMEDIA_SOUND_EFFECT = 771818496;
    public static final int SETTING_FUNC_SOFT_BUTTON_SOUND_TYPE = 771883264;
    public static final int SETTING_FUNC_SOUND_WARNING_VOLUME = 538771712;
    public static final int SOFT_BUTTON_SOUND_TYPE_1 = 771883265;
    public static final int SOFT_BUTTON_SOUND_TYPE_2 = 771883266;
    public static final int SOFT_BUTTON_SOUND_TYPE_3 = 771883267;
    public static final int SOFT_BUTTON_SOUND_TYPE_OFF = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AcousticEffectMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AudioFunction {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SoftButtonSoundType {
    }
}
