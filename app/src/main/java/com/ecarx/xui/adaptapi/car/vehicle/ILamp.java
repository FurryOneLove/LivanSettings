package com.ecarx.xui.adaptapi.car.vehicle;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface ILamp {
    public static final int FCS_IHBC_STATUS = 721486081;
    public static final int LAMP_FLASHER_ANIM_CONTROL_FASHION = 721486338;
    public static final int LAMP_FLASHER_ANIM_CONTROL_NORMAL = 721486337;
    public static final int LAMP_FLASHER_ANIM_CONTROL_OFF = 0;
    public static final int LOW_BEAM_VERTICAL_LEVEL_0 = 0;
    public static final int LOW_BEAM_VERTICAL_LEVEL_1 = 721551873;
    public static final int LOW_BEAM_VERTICAL_LEVEL_2 = 721551874;
    public static final int LOW_BEAM_VERTICAL_LEVEL_3 = 721551875;
    public static final int LOW_BEAM_VERTICAL_LEVEL_4 = 721551876;
    public static final int LOW_BEAM_VERTICAL_LEVEL_5 = 721551877;
    public static final int LR_TRAFFIC_LIGHT_LEFT = 721551617;
    public static final int LR_TRAFFIC_LIGHT_OFF = 0;
    public static final int LR_TRAFFIC_LIGHT_RIGHT = 721551618;
    public static final int READING_LAMP_ASSOCIATED_WITH_DOOR_CONTROL = 721682688;
    public static final int SETTING_FUNC_AUTO_TRAILER_LAMP_CHECK = 537135872;
    public static final int SETTING_FUNC_BLN_SWITCH = 721617664;
    public static final int SETTING_FUNC_LAMP_ACTIVE_HIGH_BEAM_CONTROL = 721486080;
    public static final int SETTING_FUNC_LAMP_ADAPTIVE_FRONT_LIGHT = 537136384;
    public static final int SETTING_FUNC_LAMP_APPROACH_LIGHT = 537135360;
    public static final int SETTING_FUNC_LAMP_AUTOLIGHT = 537133824;
    public static final int SETTING_FUNC_LAMP_AUTOMATIC_COURTESY_LIGHT = 537134592;
    public static final int SETTING_FUNC_LAMP_AUX_HIGHBEAM = 537134080;
    public static final int SETTING_FUNC_LAMP_BENDINGLIGHT = 537134336;
    public static final int SETTING_FUNC_LAMP_CORNERING_LIGHT = 537135616;
    public static final int SETTING_FUNC_LAMP_DAYTIME_RUNNING_LIGHT = 537135104;
    public static final int SETTING_FUNC_LAMP_EXTERIOR_LIGHT_CONTROL = 537136640;
    public static final int SETTING_FUNC_LAMP_FLASHER_ANIM_CONTROL = 721486336;
    public static final int SETTING_FUNC_LAMP_HIGHBEAM_ACTIVE = 537133568;
    public static final int SETTING_FUNC_LAMP_HOME_SAFE_LIGHT = 537134848;
    public static final int SETTING_FUNC_LAMP_LOW_BEAM_VERTICAL_LEVEL = 721551872;
    public static final int SETTING_FUNC_LAMP_LOW_BEAM_VERTICAL_LEVEL_ADJUST = 721552128;
    public static final int SETTING_FUNC_LAMP_LR_TRAFFIC_LIGHT = 721551616;
    public static final int SETTING_FUNC_LAMP_TRIPLE_FLASH = 537133312;
    public static final int SETTING_FUNC_MANUAL_TRAILER_LAMP_CHECK = 537136128;

    @VendorDefinition(author = "@ECARX", date = "2021-03-30", project = "Smart", requirement = "")
    public static final int SETTING_FUNC_TRAILER_LAMP = 721617920;
    public static final int SETTING_FUNC_WELCOME_LIGHT = 721617152;
    public static final int SETTING_FUNC_WELCOME_LIGHT_MODE = 721617408;
    public static final int WELCOME_LIGHT_MODE_1 = 721617409;
    public static final int WELCOME_LIGHT_MODE_2 = 721617410;
    public static final int WELCOME_LIGHT_MODE_3 = 721617411;
    public static final int WELCOME_LIGHT_MODE_4 = 721617412;
    public static final int WELCOME_LIGHT_MODE_5 = 721617413;
    public static final int WELCOME_LIGHT_MODE_6 = 721617414;
    public static final int WELCOME_LIGHT_MODE_OFF = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface FlasherAnimControlMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LRTrafficLightMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LampFunction {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LowBeamVerticaLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WelComeLightMode {
    }
}
