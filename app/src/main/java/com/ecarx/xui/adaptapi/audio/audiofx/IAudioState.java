package com.ecarx.xui.adaptapi.audio.audiofx;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IAudioState {
    public static final int AUDIO_PROVIDER_BOSE = 2;
    public static final int AUDIO_PROVIDER_DEFAULT = 0;
    public static final int AUDIO_PROVIDER_HARMAN = 1;
    public static final int NAVI_VOICE_MIX_AUTO = 1;
    public static final int NAVI_VOICE_MIX_DIRECTLY = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AudioProvider {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NaviVoiceMixMode {
    }

    int getNaviVoiceMixMode();

    int getSoundStageOptimizedSeat();

    boolean isBootUpMusicOn();
}
