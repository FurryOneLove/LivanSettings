package com.ecarx.xui.adaptapi.car.vendor;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@VendorDefinition(author = "@ECARX", date = "2020-07-02", project = "ALL")
/* loaded from: classes.dex */
public interface IVendorFuncs {

    @VendorDefinition(author = "@ECARX", date = "2021-01-14", project = "KX11-E02", requirement = "XQ2020091666751")
    public static final int CUSTOM_KEY_TYPE_360_PANORAMA = -2147417854;

    @VendorDefinition(author = "@ECARX", date = "2021-01-14", project = "KX11-E02", requirement = "XQ2020091666751")
    public static final int CUSTOM_KEY_TYPE_CAR_SETTING = -2147417849;

    @VendorDefinition(author = "@ECARX", date = "2021-01-14", project = "KX11-E02", requirement = "XQ2020091666751")
    public static final int CUSTOM_KEY_TYPE_COLLECT_FAV = -2147417850;

    @VendorDefinition(author = "@ECARX", date = "2021-01-14", project = "KX11-E02", requirement = "XQ2020091666751")
    public static final int CUSTOM_KEY_TYPE_DIM_FULL_SCREEN_MAP = -2147417852;

    @VendorDefinition(author = "@ECARX", date = "2021-01-14", project = "KX11-E02", requirement = "XQ2020091666751")
    public static final int CUSTOM_KEY_TYPE_DVR = -2147417855;

    @VendorDefinition(author = "@ECARX", date = "2021-01-14", project = "KX11-E02", requirement = "XQ2020091666751")
    public static final int CUSTOM_KEY_TYPE_NAVIGATION = -2147417853;

    @VendorDefinition(author = "@ECARX", date = "2021-01-14", project = "KX11-E02", requirement = "XQ2020091666751")
    public static final int CUSTOM_KEY_TYPE_SOUND_SWITCH = -2147417851;

    @VendorDefinition(author = "@ECARX", date = "2021-01-14", project = "KX11-E02", requirement = "XQ2020091666751")
    public static final int CUSTOM_KEY_TYPE_WHEEL_HEAT = -2147417848;

    @VendorDefinition(author = "@ECARX", date = "2021-01-14", project = "KX11-E02", requirement = "XQ2020091666751")
    public static final int VENDOR_FUNC_CUSTOM_KEY = -2147417856;

    @VendorDefinition(author = "@ECARX", date = "2021-01-14", project = "KX11-E02", requirement = "XQ2020091666751")
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomKeyType {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface VendorFunctionFlt {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface VendorFunctionInt {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface VendorFunctionValues {
    }
}
