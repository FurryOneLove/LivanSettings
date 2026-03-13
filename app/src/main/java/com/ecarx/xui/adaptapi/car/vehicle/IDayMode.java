package com.ecarx.xui.adaptapi.car.vehicle;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IDayMode {
    public static final int SETTING_FUNC_BACKLIGHT_LINKAGE = 687931648;
    public static final int SETTING_FUNC_BRIGHTNESS_BACKLIGHT = 687997184;
    public static final int SETTING_FUNC_BRIGHTNESS_BACKLIGHT_MAX = 687997440;
    public static final int SETTING_FUNC_BRIGHTNESS_BACKLIGHT_MIN = 687997696;
    public static final int SETTING_FUNC_BRIGHTNESS_BACKLIGHT_STEP = 687997952;
    public static final int SETTING_FUNC_BRIGHTNESS_DAY = 538247936;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_BRIGHTNESS_DIM = 687998208;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_BRIGHTNESS_DIM_MAX = 687998464;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_BRIGHTNESS_DIM_MIN = 687998720;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_BRIGHTNESS_DIM_STEP = 687998976;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_BRIGHTNESS_FLOODLIGHT = 687999232;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_BRIGHTNESS_FLOODLIGHT_MAX = 687999488;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_BRIGHTNESS_FLOODLIGHT_MIN = 687999744;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_BRIGHTNESS_FLOODLIGHT_STEP = 688000000;
    public static final int SETTING_FUNC_BRIGHTNESS_MAX = 538248448;
    public static final int SETTING_FUNC_BRIGHTNESS_MIN = 538248704;
    public static final int SETTING_FUNC_BRIGHTNESS_NIGHT = 538248192;
    public static final int SETTING_FUNC_BRIGHTNESS_STEP = 538248960;
    public static final int SETTING_FUNC_DAYMODE_SETTING = 538247424;
    public static final int SETTING_FUNC_DAYMODE_SYNC = 538247680;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_ELECTRIC_REAR_VIEW_MIRROR_DISPLAYS = 688000256;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_ELECTRIC_REAR_VIEW_MIRROR_DISPLAYS_MAX = 688000768;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_ELECTRIC_REAR_VIEW_MIRROR_DISPLAYS_MIN = 688000512;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_ELECTRIC_REAR_VIEW_MIRROR_DISPLAYS_STEP = 688062720;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DayModeFunction {
    }
}
