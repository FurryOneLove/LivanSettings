package com.ecarx.xui.adaptapi.audio.audiofx;

import android.content.Context;
import com.ecarx.xui.adaptapi.AdaptAPI;
import com.ecarx.xui.adaptapi.FunctionStatus;
import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public abstract class Audio extends AdaptAPI {
    public static final int AUDIO_SETTING_BOOT_UP_MUSIC = 4098;
    public static final int AUDIO_SETTING_NAVI_MIX_MODE = 4097;
    public static final int AUDIO_SETTING_SEAT_SOUND_OPTIMIZE = 4099;
    public static final int AUDIO_SYSTEM_LEVEL_2D_SOURROUND = 2;
    public static final int AUDIO_SYSTEM_LEVEL_3D_SOURROUND = 4;
    public static final int AUDIO_SYSTEM_LEVEL_ENTRY = 1;
    public static final int SOURROUND_SOUND_LEVEL_HIGH = 64;
    public static final int SOURROUND_SOUND_LEVEL_LOW = 16;
    public static final int SOURROUND_SOUND_LEVEL_MEDIUM = 32;

    @Retention(RetentionPolicy.SOURCE)
    @interface AudioSettings {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-06-30", project = "Lambda")
    @Retention(RetentionPolicy.SOURCE)
    @interface AudioSystemLevel {
    }

    public interface IAudioSettingListener {
        void onAudioSettingStateChanged(int i);
    }

    @VendorDefinition(author = "@ECARX", date = "2021-06-30", project = "Lambda")
    @Retention(RetentionPolicy.SOURCE)
    @interface SourroundSoundLevel {
    }

    public abstract int getAudioProvider();

    public abstract IAudioState getAudioState();

    @VendorDefinition(author = "@ECARX", date = "2021-06-30", project = "Lambda")
    public abstract int getAudioSystemLevel();

    public abstract ICompensation getCompensation();

    public abstract IEqualizer getEqualizer();

    public abstract IFaderBalance getFaderBalance();

    public abstract IHarmanEqualizer getHarmanEqualizer();

    @VendorDefinition(author = "@ECARX", date = "2021-06-30", project = "Lambda")
    public abstract int getSourroundSoundLevel();

    public abstract FunctionStatus isAudioSettingSupported(int i);

    @VendorDefinition(author = "@ECARX", date = "2021-06-30", project = "Lambda")
    public abstract boolean isSourroundSoundOn();

    public abstract boolean registerAudioSettingListener(IAudioSettingListener iAudioSettingListener);

    public abstract void setBootUpMusicOnOff(boolean z);

    public abstract void setNaviVoiceMixMode(int i);

    public abstract void setSeatSoundStageOptimize(int i);

    @VendorDefinition(author = "@ECARX", date = "2021-06-30", project = "Lambda")
    public abstract void setSourroundSoundLevel(int i);

    @VendorDefinition(author = "@ECARX", date = "2021-06-30", project = "Lambda")
    public abstract void setSourroundSoundOnOff(boolean z);

    public abstract boolean unregisterAudioSettingListener(IAudioSettingListener iAudioSettingListener);

    public static Audio create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.audio.impl.AudioImpl");
            Constructor c = clazz.getConstructor(Context.class);
            return (Audio) c.newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
