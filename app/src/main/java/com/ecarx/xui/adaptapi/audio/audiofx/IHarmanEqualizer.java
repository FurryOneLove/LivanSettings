package com.ecarx.xui.adaptapi.audio.audiofx;

import com.ecarx.xui.adaptapi.FunctionStatus;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IHarmanEqualizer {
    public static final int HARMAN_BAND_HIGH = 3;
    public static final int HARMAN_BAND_LOW = 1;
    public static final int HARMAN_BAND_MID = 2;
    public static final int HARMAN_SETTING_BAND_LEVEL = 8195;
    public static final int HARMAN_SETTING_CLARIFI = 8193;
    public static final int HARMAN_SETTING_SURROUND_SOUND = 8194;

    @Retention(RetentionPolicy.SOURCE)
    public @interface HarmanBand {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface HarmanSettings {
    }

    public interface IHarmanSettingListener {
        void onHarmanSettingStateChanged(int i);
    }

    short getBandLevel(int i);

    short[] getBandLevelRange();

    boolean isHarmanClariFiOnOff();

    FunctionStatus isHarmanSettingSupported(int i);

    boolean isSurroundSoundOnOff();

    boolean registerHarmanSettingListener(IHarmanSettingListener iHarmanSettingListener);

    void setBandLevel(int i, short s);

    boolean setHarmanClariFiOnOff(boolean z);

    boolean setSurroundSoundOnOff(boolean z);

    boolean unregisterHarmanSettingListener(IHarmanSettingListener iHarmanSettingListener);
}
