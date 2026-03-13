package com.ecarx.xui.adaptapi.car.vehicle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IHUD {
    public static final int SETTING_FUNC_HUD_ACTIVE = 537985280;
    public static final int SETTING_FUNC_HUD_ANGLE_ADJUST = 654378752;
    public static final int SETTING_FUNC_HUD_ANGLE_RESET = 654379008;
    public static final int SETTING_FUNC_HUD_AR_ENGINE = 654443008;
    public static final int SETTING_FUNC_HUD_BRIGHTNESS = 654377216;
    public static final int SETTING_FUNC_HUD_BRIGHTNESS_ADJUST = 654378240;
    public static final int SETTING_FUNC_HUD_BRIGHTNESS_MAX = 654377472;
    public static final int SETTING_FUNC_HUD_BRIGHTNESS_MIN = 654377728;
    public static final int SETTING_FUNC_HUD_BRIGHTNESS_STEP = 654377984;
    public static final int SETTING_FUNC_HUD_CALIBRATION = 537985536;
    public static final int SETTING_FUNC_HUD_DISPLAY_BTPHONE = 654509056;
    public static final int SETTING_FUNC_HUD_DISPLAY_DRIVE_ENVIRONMENT = 654509312;
    public static final int SETTING_FUNC_HUD_DISPLAY_MEIDA = 654508544;
    public static final int SETTING_FUNC_HUD_DISPLAY_NAVI = 654508800;
    public static final int SETTING_FUNC_HUD_DISPLAY_SAFETY = 654508288;
    public static final int SETTING_FUNC_HUD_POSITION_ADJUST = 654378496;
    public static final int SETTING_FUNC_HUD_SNOW_MODE = 654442752;

    @Retention(RetentionPolicy.SOURCE)
    public @interface HUDFunction {
    }
}
