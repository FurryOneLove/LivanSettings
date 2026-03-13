package com.ecarx.xui.adaptapi.car.vehicle;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IAmbienceLight {

    @VendorDefinition(author = "@ECARX", date = "2021-02-20", project = "KXEX")
    public static final int AMBIENCE_LIGHT_CUSTOM_MODE_BREATHE = 705167619;

    @VendorDefinition(author = "@ECARX", date = "2021-02-20", project = "KXEX")
    public static final int AMBIENCE_LIGHT_CUSTOM_MODE_GRADIENTS = 705167618;

    @VendorDefinition(author = "@ECARX", date = "2021-02-20", project = "KXEX")
    public static final int AMBIENCE_LIGHT_CUSTOM_MODE_SOLID_COLOR = 705167617;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_APPLE_GREEN = 704709132;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_BLUE = 704709126;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_GREEN = 704709124;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_ICE_BLUE = 704709129;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_INDIGO = 704709125;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_OFF = 0;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_ORANGE = 704709122;

    @VendorDefinition(author = "@ECARX", date = "2021-05-14", project = "E02")
    public static final int AMBIENCE_LIGHT_THEME_COLOR_PINK = 704709133;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_RED = 704709121;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_SPANISH_RED = 704709131;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_SUN_RED = 704709130;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_VIOLET = 704709127;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_WHITE = 704709128;
    public static final int AMBIENCE_LIGHT_THEME_COLOR_YELLOW = 704709123;

    @VendorDefinition(author = "@ECARX", date = "2021-06-21", project = "smart")
    public static final int AMBIENCE_LIGHT_THEME_MODE_AGILE = 705167620;

    @VendorDefinition(author = "@ECARX", date = "2021-06-21", project = "smart")
    public static final int AMBIENCE_LIGHT_THEME_MODE_EV = 705167622;

    @VendorDefinition(author = "@ECARX", date = "2021-06-21", project = "smart")
    public static final int AMBIENCE_LIGHT_THEME_MODE_FASHION = 705167621;

    @VendorDefinition(author = "@ECARX", date = "2021-06-21", project = "smart")
    public static final int AMBIENCE_LIGHT_THEME_MODE_LIBERATING = 705167619;

    @VendorDefinition(author = "@ECARX", date = "2021-06-21", project = "smart")
    public static final int AMBIENCE_LIGHT_THEME_MODE_RADICAL = 705167617;

    @VendorDefinition(author = "@ECARX", date = "2021-06-21", project = "smart")
    public static final int AMBIENCE_LIGHT_THEME_MODE_SIMPLE = 705167618;

    @VendorDefinition(author = "@ECARX", date = "2020-10-31", project = "KXEX")
    public static final int AMBIENCE_LIGHT_ZONE_ALL = 537526528;

    @VendorDefinition(author = "@ECARX", date = "2020-10-31", project = "KXEX")
    public static final int AMBIENCE_LIGHT_ZONE_FRONT = 537526529;

    @VendorDefinition(author = "@ECARX", date = "2020-10-31", project = "KXEX")
    public static final int AMBIENCE_LIGHT_ZONE_HEADREST = 537526530;

    @VendorDefinition(author = "@ECARX", date = "2020-10-31", project = "KXEX")
    public static final int AMBIENCE_LIGHT_ZONE_REAR = 537526531;
    public static final int MOOD_LIGHT_COLOR_BLUE = 705036806;
    public static final int MOOD_LIGHT_COLOR_GREEN = 705036804;
    public static final int MOOD_LIGHT_COLOR_INDIGO = 705036805;
    public static final int MOOD_LIGHT_COLOR_OFF = 0;
    public static final int MOOD_LIGHT_COLOR_ORANGE = 705036802;
    public static final int MOOD_LIGHT_COLOR_RED = 705036801;
    public static final int MOOD_LIGHT_COLOR_VIOLET = 705036807;
    public static final int MOOD_LIGHT_COLOR_WHITE = 705036808;
    public static final int MOOD_LIGHT_COLOR_YELLOW = 705036803;
    public static final int MOOD_LIGHT_MODE_COLOR = 705037058;
    public static final int MOOD_LIGHT_MODE_OFF = 0;
    public static final int MOOD_LIGHT_MODE_TEMP = 705037057;
    public static final int MUSIC_SHOW_MODE_NORMAL = 704972802;
    public static final int MUSIC_SHOW_MODE_OFF = 0;
    public static final int MUSIC_SHOW_MODE_PASSIONATE = 704972801;
    public static final int MUSIC_SHOW_MODE_SUBDUED = 704972803;

    @VendorDefinition(author = "@ECARX", date = "2021-06-01", project = "3BE02", requirement = "")
    public static final int SETTING_FUNC_ALCM_SOUND_PCT = 705103360;

    @VendorDefinition(author = "@ECARX", date = "2021-06-01", project = "3BE02", requirement = "")
    public static final int SETTING_FUNC_ALCM_VOICE_MODE = 705103104;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_BOTZONES = 537527040;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_BOTZONES_COLOR_SET = 704774400;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_BOTZONES_INTENSITY = 704774656;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_BRIGHTNESS_DRIVING = 537528064;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_BRIGHTNESS_STATIONARY = 537527808;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "smart", requirement = "")
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_CLIMATE = 705167872;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_COLOR_SET = 537528576;

    @VendorDefinition(author = "@ECARX", date = "2021-02-20", project = "KXEX")
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_EFFECT_SET = 705167616;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_ENDURANCE_MIL_REMINDER = 704971520;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_EXPERIENCE = 537526528;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_GOODBYE_SHOW = 704971264;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "smart", requirement = "")
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_ICHARGING_REMIND = 705168128;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_INTENSITY_SET = 704708864;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_INTERACTIVE_EFFECT = 537528320;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_MAINCOLOR = 537526784;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_MAINZONES = 537527552;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_MAINZONES_COLOR_SET = 704905472;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_MAINZONES_INTENSITY = 704905728;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_MUSIC_SHOW_MODE = 704972800;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_OVER_SPEED_WARNING = 704972288;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_PHONE_CALL_REMINDER = 704971776;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_STANDSTILL = 704972032;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_THEME_COLOR = 704709120;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_TOPZONES = 537527296;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_TOPZONES_COLOR_SET = 704839936;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_TOPZONES_INTENSITY = 704840192;

    @VendorDefinition(author = "@ECARX", date = "2021-06-01", project = "SMART", requirement = "")
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_VOICE = 704974080;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_WELCOME_SHOW = 704971008;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_WELCOME_SHOW_MODE = 704972544;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "smart")
    public static final int SETTING_FUNC_AMBIENCE_THEME_MODE = 705168384;
    public static final int SETTING_FUNC_AMBIENCE_ZONES_SYNC = 704709632;

    @VendorDefinition(author = "@ECARX", date = "2021-06-01", project = "3BE02", requirement = "")
    public static final int SETTING_FUNC_BLUETOOTH_CALLING_STATUS = 705102848;
    public static final int SETTING_FUNC_BREATHE_MODE_COLOR = 704709376;
    public static final int SETTING_FUNC_MOOD_LIGHT = 705036544;
    public static final int SETTING_FUNC_MOOD_LIGHT_COLOR = 705036800;
    public static final int SETTING_FUNC_MOOD_LIGHT_MODE = 705037056;
    public static final int SETTING_FUNC_TRANSITION_END_COLOR = 705102592;
    public static final int SETTING_FUNC_TRANSITION_MODE = 705102080;
    public static final int SETTING_FUNC_TRANSITION_START_COLOR = 705102336;
    public static final int VOICE_MODE_END_ASR = 705103108;
    public static final int VOICE_MODE_END_NLU = 705103110;
    public static final int VOICE_MODE_END_RECORD = 705103106;
    public static final int VOICE_MODE_END_TTS = 705103112;
    public static final int VOICE_MODE_NLU_IMPL_FAIL = 705103114;
    public static final int VOICE_MODE_NLU_IMPL_SUCCESS = 705103113;
    public static final int VOICE_MODE_RESERVED = 705103115;
    public static final int VOICE_MODE_START_ASR = 705103107;
    public static final int VOICE_MODE_START_NLU = 705103109;
    public static final int VOICE_MODE_START_RECORD = 705103105;
    public static final int VOICE_MODE_START_TTS = 705103111;
    public static final int VOICE_PCT_NO_VOICE = 705103487;
    public static final int VOICE_PCT_RESERVED = 705103461;
    public static final int WELCOME_SHOW_MODE_NORMAL = 704972546;
    public static final int WELCOME_SHOW_MODE_OFF = 0;
    public static final int WELCOME_SHOW_MODE_PASSIONATE = 704972545;
    public static final int WELCOME_SHOW_MODE_SUBDUED = 704972547;

    @VendorDefinition(author = "@ECARX", date = "2021-02-20", project = "KXEX")
    @Retention(RetentionPolicy.SOURCE)
    public @interface AmbienceLightCustomModeEffect {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AmbienceLightFunction {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "Smart")
    @Retention(RetentionPolicy.SOURCE)
    public @interface AmbienceLightThemeModeEffect {
    }

    @VendorDefinition(author = "@ECARX", date = "2020-10-31", project = "ALL")
    @Retention(RetentionPolicy.SOURCE)
    public @interface AmbienceLightZone {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AmbienceThemeColors {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ModeLightColors {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ModeLightModes {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MusicShowModes {
    }

    @VendorDefinition(author = "@ECARX", date = "2020-06-01", project = "3BE02")
    @Retention(RetentionPolicy.SOURCE)
    public @interface VoiceMode {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-06-01", project = "3BE02")
    @Retention(RetentionPolicy.SOURCE)
    public @interface VoicePct {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WelcomeShowModes {
    }
}
