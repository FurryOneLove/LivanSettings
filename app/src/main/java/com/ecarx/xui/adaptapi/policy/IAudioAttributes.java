package com.ecarx.xui.adaptapi.policy;

import android.view.Display;
import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IAudioAttributes {
    public static final String CONTENT_TYPE_MD_AM = "AM";
    public static final String CONTENT_TYPE_MD_AUDIO_ASSISTANT = "AUDIO_ASSISTAN";
    public static final String CONTENT_TYPE_MD_AUX = "AUX";
    public static final String CONTENT_TYPE_MD_BCALL = "BCALL";
    public static final String CONTENT_TYPE_MD_BLUETOOTH_HFP = "BLUETOOTH_HFP";
    public static final String CONTENT_TYPE_MD_BLUETOOTH_RINGTONE = "BLUETOOTH_RINGTONE";
    public static final String CONTENT_TYPE_MD_BT_AUDIO = "BT_AUDIO";
    public static final String CONTENT_TYPE_MD_DONOTDISTURB = "DONOTDISTURB";
    public static final String CONTENT_TYPE_MD_ECALL = "ECALL";
    public static final String CONTENT_TYPE_MD_FM = "FM";
    public static final String CONTENT_TYPE_MD_ICALL = "ICALL";
    public static final String CONTENT_TYPE_MD_KTV = "KTV";
    public static final String CONTENT_TYPE_MD_MOVIE = "MOVIE";
    public static final String CONTENT_TYPE_MD_MUSIC = "MUSIC";
    public static final String CONTENT_TYPE_MD_NAVI_GUIDANCE = "NAVI_GUIDANCE";
    public static final String CONTENT_TYPE_MD_NAVI_HINT = "NAVI_HINT";
    public static final String CONTENT_TYPE_MD_ONCALL = "ONCALL";
    public static final String CONTENT_TYPE_MD_PHONE_COMING_HINT = "PHONE_COMING_HINT";
    public static final String CONTENT_TYPE_MD_TTS = "TTS";
    public static final String CONTENT_TYPE_MD_UNKNOWN = "UNKNOWN";
    public static final String USAGE_MD_ASSISTANT = "ASSISTANT";
    public static final String USAGE_MD_BICALL = "BICALL";
    public static final String USAGE_MD_BT_AUDIO = "BT_AUDIO";
    public static final String USAGE_MD_DONOTDISTURB = "DONOTDISTURB";
    public static final String USAGE_MD_ECALL = "ECALL";
    public static final String USAGE_MD_KTV = "KTV";
    public static final String USAGE_MD_MEDIA = "MEDIA";

    @VendorDefinition(author = "@ECARX", date = "2021-04-27", project = "Lambda")
    public static final String USAGE_MD_MEDIA_REAR = "REAR_MEDIA";
    public static final String USAGE_MD_NAVIGATION_GUIDANCE = "NAVIGATION_GUIDANCE";
    public static final String USAGE_MD_NAVIGATION_HINT = "NAVIGATION_HINT";
    public static final String USAGE_MD_RADIO = "RADIO";
    public static final String USAGE_MD_TTS = "TTS";
    public static final String USAGE_MD_VOICE_COMMUNICATION = "VOICE_COMMUNICATION";
    public static final String USAGE_MD_VOICE_COMMUNICATION_SIGNALLING = "VOICE_COMMUNICATION_SIGNALLING";

    @Retention(RetentionPolicy.SOURCE)
    public @interface ContentType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Usage {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-01-26", project = "KX11", requirement = "")
    int getActiveAudioFocusType();

    int getAudioAtrributesContentType(String str);

    int getAudioAtrributesUsage(String str);

    int getAudioAtrributesUsage(String str, Display display);
}
