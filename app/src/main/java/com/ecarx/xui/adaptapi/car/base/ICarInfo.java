package com.ecarx.xui.adaptapi.car.base;

import android.view.Display;
import com.ecarx.xui.adaptapi.FunctionStatus;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

/* loaded from: classes.dex */
public interface ICarInfo {
    public static final int CONFIG_INFO_360CAM = 8389888;
    public static final int CONFIG_INFO_DVR = 8389376;
    public static final int CONFIG_INFO_DVR_INNERCAM = 8389632;
    public static final int CONFIG_INFO_EN_START_BUTTON = 8392192;
    public static final int CONFIG_INFO_FACE_CAM = 8391936;
    public static final int CONFIG_INFO_FINGERPRINT = 8389120;
    public static final int CONFIG_INFO_RADAR = 8391424;
    public static final int CONFIG_INFO_REARVIEW_CAM = 8391680;
    public static final int CONFIG_INFO_REAR_CAM = 8390144;
    public static final int CONFIG_INFO_SUNROOF = 8390912;
    public static final int CONFIG_INFO_TCAM = 8391168;
    public static final int CONFIG_INFO_TEM = 8388864;
    public static final int CONFIG_INFO_VALUE_CONFIG = 8388610;
    public static final int CONFIG_INFO_VALUE_FAULT = 8388862;
    public static final int CONFIG_INFO_VALUE_NOT_CONFIG = 8388609;
    public static final int CONFIG_INFO_VALUE_PRELOAD = 8388611;
    public static final int CONFIG_INFO_VALUE_UNKNOWN = 8388863;
    public static final int CONFIG_INFO_WIFI = 8390400;
    public static final int CONFIG_INFO_WPC = 8390656;
    public static final int DISPLAY_TYPE_DIM = -2147483647;
    public static final int DISPLAY_TYPE_HUD = -2147483646;
    public static final int DRIVER_SIDE_CENTER = 1049347;
    public static final int DRIVER_SIDE_LEFT = 1049345;
    public static final int DRIVER_SIDE_RIGHT = 1049346;
    public static final int DRIVER_SIDE_UNKNOWN = 1049599;
    public static final int DRIVE_MODE_AWD = 1049603;
    public static final int DRIVE_MODE_FRONT = 1049601;
    public static final int DRIVE_MODE_REAR = 1049602;
    public static final int DRIVE_MODE_UNKNOWN = 1049855;
    public static final int EV_CONNECTOR_TYPE_CHADEMO = 4194819;
    public static final int EV_CONNECTOR_TYPE_COMBO_1 = 4194820;
    public static final int EV_CONNECTOR_TYPE_COMBO_2 = 4194821;
    public static final int EV_CONNECTOR_TYPE_GBT = 4194825;
    public static final int EV_CONNECTOR_TYPE_J1772 = 4194817;
    public static final int EV_CONNECTOR_TYPE_MENNEKES = 4194818;
    public static final int EV_CONNECTOR_TYPE_TESLA_HPWC = 4194823;
    public static final int EV_CONNECTOR_TYPE_TESLA_ROADSTER = 4194822;
    public static final int EV_CONNECTOR_TYPE_TESLA_SUPERCHARGER = 4194824;
    public static final int EV_CONNECTOR_TYPE_UNKNOWN = 4195071;
    public static final int FLT_INFO_EV_BATTERY_CAPACITY = 2097664;
    public static final int FLT_INFO_FUEL_CAPACITY = 2097408;
    public static final int FLT_INFO_MAX_LIMITED_SPEED = 2098176;
    public static final int FLT_INFO_VEHICLE_WEIGHT = 2097920;
    public static final int FUEL_TYPE_BIODIESEL = 1048837;
    public static final int FUEL_TYPE_CNG = 1048840;
    public static final int FUEL_TYPE_DIESEL_1 = 1048835;
    public static final int FUEL_TYPE_DIESEL_2 = 1048836;
    public static final int FUEL_TYPE_E85 = 1048838;
    public static final int FUEL_TYPE_ELECTRIC = 1048842;
    public static final int FUEL_TYPE_HYDROGEN = 1048843;
    public static final int FUEL_TYPE_LEADED = 1048834;
    public static final int FUEL_TYPE_LNG = 1048841;
    public static final int FUEL_TYPE_LPG = 1048839;
    public static final int FUEL_TYPE_UNKNOWN = 1049087;
    public static final int FUEL_TYPE_UNLEADED = 1048833;
    public static final int INFO_TYPE_CONFIG = 8388608;
    public static final int INFO_TYPE_FLT = 2097152;
    public static final int INFO_TYPE_INT = 1048576;
    public static final int INFO_TYPE_INTS = 4194304;
    public static final int INFO_TYPE_MAP = 9437184;
    public static final int INFO_TYPE_STR = 3145728;
    public static final int INTS_INFO_EV_CONNECTOR_TYPES = 4194816;
    public static final int INTS_INFO_FUEL_TYPES = 1048832;
    public static final int INTS_INFO_SCENE_MODES = 4195072;
    public static final int INT_INFO_DRIVER_SIDE = 1049344;
    public static final int INT_INFO_DRIVE_MODE = 1049600;

    @Deprecated
    public static final int INT_INFO_FUEL_TYPES = 1048832;
    public static final int INT_INFO_MIC_TOTAL_COUNT = 1049856;
    public static final int INT_INFO_SPEAKER_TOTAL_COUNT = 1050624;
    public static final int INT_INFO_VEHICLE_TYPES = 1049088;
    public static final int MAP_INFO_EV_SLOPE_DOWN_ENERGY_COEFFICIENT = 9437696;
    public static final int MAP_INFO_EV_SLOPE_RISE_ENERGY_COEFFICIENT = 9437440;
    public static final int MAP_INFO_EV_SPEED_DOWN_ENERGY_COEFFICIENT = 9438208;
    public static final int MAP_INFO_EV_SPEED_RELATE_WEIGHT = 9438464;
    public static final int MAP_INFO_EV_SPEED_RISE_ENERGY_COEFFICIENT = 9437952;
    public static final int SCENE_MODE_AWAKENING = 4195074;
    public static final int SCENE_MODE_PARENT_CHILD = 4195075;
    public static final int SCENE_MODE_SMOKING = 4195073;
    public static final int SCENE_MODE_UNKNOWN = 4195327;
    public static final int SCENE_MODE_YUEDONG = 4195076;
    public static final int VEHICLE_TYPE_BEV = 1049096;
    public static final int VEHICLE_TYPE_EREV = 1049092;
    public static final int VEHICLE_TYPE_FCEV = 1049093;
    public static final int VEHICLE_TYPE_FCV = 1049094;
    public static final int VEHICLE_TYPE_FUEL = 1049089;
    public static final int VEHICLE_TYPE_HEV = 1049090;
    public static final int VEHICLE_TYPE_MHEV = 1049095;
    public static final int VEHICLE_TYPE_PHEV = 1049091;
    public static final int VEHICLE_TYPE_UNKNOWN = 1049343;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ConfigInfo {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ConfigValue {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface DisplayType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DriveModes {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DriverSide {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EVConnectorTypes {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface FltInfo {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FuelTypes {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface InfoType {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface IntInfo {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface IntValues {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface IntsInfo {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface MapInfo {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SceneModes {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface StrInfo {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface VehicleTypes {
    }

    int getCarInfoConfig(int i);

    float getCarInfoFloat(int i);

    int getCarInfoInt(int i);

    int[] getCarInfoInts(int i);

    Map getCarInfoMap(int i);

    String getCarInfoString(int i);

    Display getPresentationDisplay(int i);

    FunctionStatus isCarInfoSupported(int i);
}
