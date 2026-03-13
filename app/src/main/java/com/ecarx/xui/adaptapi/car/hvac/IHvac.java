package com.ecarx.xui.adaptapi.car.hvac;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IHvac {
    public static final int AFUSWITCHRESP_SIGNAL_LOST = 269156609;
    public static final int AFUSWITCHRESP_SIGNAL_LOST_TYPE = 269156609;
    public static final int AFUSWITCHRESP_SIGNAL_UNLOST = 269156611;
    public static final int AIR_FRAGRANCE_LEVEL_1 = 269157121;
    public static final int AIR_FRAGRANCE_LEVEL_2 = 269157122;
    public static final int AIR_FRAGRANCE_LEVEL_3 = 269157123;
    public static final int AIR_FRAGRANCE_LEVEL_OFF = 0;
    public static final int AIR_FRAGRANCE_OFF = 0;
    public static final int AIR_FRAGRANCE_TASTE1 = 269156865;
    public static final int AIR_FRAGRANCE_TASTE10 = 269156874;
    public static final int AIR_FRAGRANCE_TASTE11 = 269156875;
    public static final int AIR_FRAGRANCE_TASTE12 = 269156876;
    public static final int AIR_FRAGRANCE_TASTE13 = 269156877;
    public static final int AIR_FRAGRANCE_TASTE14 = 269156878;
    public static final int AIR_FRAGRANCE_TASTE15 = 269156879;
    public static final int AIR_FRAGRANCE_TASTE16 = 269156880;
    public static final int AIR_FRAGRANCE_TASTE17 = 269156881;
    public static final int AIR_FRAGRANCE_TASTE18 = 269156882;
    public static final int AIR_FRAGRANCE_TASTE19 = 269156883;
    public static final int AIR_FRAGRANCE_TASTE2 = 269156866;
    public static final int AIR_FRAGRANCE_TASTE3 = 269156867;
    public static final int AIR_FRAGRANCE_TASTE4 = 269156868;
    public static final int AIR_FRAGRANCE_TASTE5 = 269156869;
    public static final int AIR_FRAGRANCE_TASTE6 = 269156870;
    public static final int AIR_FRAGRANCE_TASTE7 = 269156871;
    public static final int AIR_FRAGRANCE_TASTE8 = 269156872;
    public static final int AIR_FRAGRANCE_TASTE9 = 269156873;
    public static final int AUTO_FAN_SETTING_HIGH = 268567043;
    public static final int AUTO_FAN_SETTING_HIGHER = 268567045;
    public static final int AUTO_FAN_SETTING_NORMAL = 268567042;
    public static final int AUTO_FAN_SETTING_QUIETER = 268567044;
    public static final int AUTO_FAN_SETTING_SILENT = 268567041;
    public static final int AUTO_SEAT_HEATING_LEVEL_1 = 268764417;
    public static final int AUTO_SEAT_HEATING_LEVEL_2 = 268764418;
    public static final int AUTO_SEAT_HEATING_LEVEL_3 = 268764419;
    public static final int AUTO_SEAT_HEATING_OFF = 0;
    public static final int AUTO_SEAT_HEATING_TIME_1 = 268764673;
    public static final int AUTO_SEAT_HEATING_TIME_2 = 268764674;
    public static final int AUTO_SEAT_HEATING_TIME_3 = 268764675;
    public static final int AUTO_SEAT_HEATING_TIME_OFF = 0;
    public static final int AUTO_SEAT_MASSAGE_LEVEL_1 = 268765185;
    public static final int AUTO_SEAT_MASSAGE_LEVEL_2 = 268765186;
    public static final int AUTO_SEAT_MASSAGE_LEVEL_3 = 268765187;
    public static final int AUTO_SEAT_MASSAGE_OFF = 0;
    public static final int AUTO_SEAT_MASSAGE_TIME_1 = 268765441;
    public static final int AUTO_SEAT_MASSAGE_TIME_2 = 268765442;
    public static final int AUTO_SEAT_MASSAGE_TIME_3 = 268765443;
    public static final int AUTO_SEAT_MASSAGE_TIME_OFF = 0;
    public static final int AUTO_SEAT_VENTILATION_LEVEL_1 = 268763905;
    public static final int AUTO_SEAT_VENTILATION_LEVEL_2 = 268763906;
    public static final int AUTO_SEAT_VENTILATION_LEVEL_3 = 268763907;
    public static final int AUTO_SEAT_VENTILATION_OFF = 0;
    public static final int AUTO_SEAT_VENTILATION_TIME_1 = 268764161;
    public static final int AUTO_SEAT_VENTILATION_TIME_2 = 268764162;
    public static final int AUTO_SEAT_VENTILATION_TIME_3 = 268764163;
    public static final int AUTO_SEAT_VENTILATION_TIME_OFF = 0;
    public static final int AUTO_STEERING_WHEEL_HEAT_HIGH = 269025795;
    public static final int AUTO_STEERING_WHEEL_HEAT_LOW = 269025793;
    public static final int AUTO_STEERING_WHEEL_HEAT_MID = 269025794;
    public static final int AUTO_STEERING_WHEEL_HEAT_OFF = 0;
    public static final int AUTO_STEERING_WHEEL_HEAT_TIME_1 = 269026049;
    public static final int AUTO_STEERING_WHEEL_HEAT_TIME_2 = 269026050;
    public static final int AUTO_STEERING_WHEEL_HEAT_TIME_3 = 269026051;
    public static final int AUTO_STEERING_WHEEL_HEAT_TIME_OFF = 0;
    public static final int BLOWING_ALL = 268894471;
    public static final int BLOWING_MODE_AUTO_SWITCH = 268894472;
    public static final int BLOWING_MODE_FACE = 268894465;
    public static final int BLOWING_MODE_FACE_AND_FRONT_WINDOW = 268894469;
    public static final int BLOWING_MODE_FACE_AND_LEG = 268894467;
    public static final int BLOWING_MODE_FRONT_WINDOW = 268894468;
    public static final int BLOWING_MODE_LEG = 268894466;
    public static final int BLOWING_MODE_LEG_AND_FRONT_WINDOW = 268894470;
    public static final int BLOWING_MODE_OFF = 0;
    public static final int CIRCULATION_AUTO = 268632323;
    public static final int CIRCULATION_INNER = 268632321;
    public static final int CIRCULATION_OFF = 0;
    public static final int CIRCULATION_OUTSIDE = 268632322;
    public static final int CLIMATE_ZONE_DUAL = 268502274;
    public static final int CLIMATE_ZONE_ELECTRIC = 268502277;
    public static final int CLIMATE_ZONE_FOUR = 268502276;
    public static final int CLIMATE_ZONE_SINGLE = 268502273;
    public static final int CLIMATE_ZONE_TRIPLE = 268502275;
    public static final int DIRECTION_MODE_AVOID = 268894978;
    public static final int DIRECTION_MODE_CUSTOM = 268894723;
    public static final int DIRECTION_MODE_FOCUS = 268894977;
    public static final int DIRECTION_MODE_OFF = 0;
    public static final int DISPLAY_WINDOW_TAB_DEFAULT = 269484801;
    public static final int DISPLAY_WINDOW_TAB_HARDWARE_POP = 269484804;
    public static final int DISPLAY_WINDOW_TAB_IONS_POP = 269484806;
    public static final int DISPLAY_WINDOW_TAB_LEFT_TEMP = 269484802;
    public static final int DISPLAY_WINDOW_TAB_NONE = 0;
    public static final int DISPLAY_WINDOW_TAB_RIGHT_TEMP = 269484803;
    public static final int DISPLAY_WINDOW_TAB_SEAT = 269484805;
    public static final int FAN_SPEED_LEVEL_1 = 268566785;
    public static final int FAN_SPEED_LEVEL_2 = 268566786;
    public static final int FAN_SPEED_LEVEL_3 = 268566787;
    public static final int FAN_SPEED_LEVEL_4 = 268566788;
    public static final int FAN_SPEED_LEVEL_5 = 268566789;
    public static final int FAN_SPEED_LEVEL_6 = 268566790;
    public static final int FAN_SPEED_LEVEL_7 = 268566791;
    public static final int FAN_SPEED_LEVEL_8 = 268566792;
    public static final int FAN_SPEED_LEVEL_9 = 268566793;
    public static final int FAN_SPEED_LEVEL_AUTO = 268566794;
    public static final int FAN_SPEED_OFF = 0;
    public static final int FRONT_WINDSHIELD_HEATING_NO_TIMEOUT = 0;
    public static final int FRONT_WINDSHIELD_HEATING_TIMEOUT = 269747713;
    public static final int HVAC_FUNC_AC = 268501760;
    public static final int HVAC_FUNC_AC_MAX = 268502016;
    public static final int HVAC_FUNC_AIR_FRAGRANCE = 269156608;
    public static final int HVAC_FUNC_AIR_FRAGRANCE_LEVEL = 269157120;

    @VendorDefinition(author = "@ECARX", date = "2021-12-01", project = "E02")
    public static final int HVAC_FUNC_AIR_FRAGRANCE_RATIO = 269158144;
    public static final int HVAC_FUNC_AIR_FRAGRANCE_TYPE = 269156864;
    public static final int HVAC_FUNC_AQS_SWITCH = 268960256;
    public static final int HVAC_FUNC_AUTO = 268501504;
    public static final int HVAC_FUNC_AUTOMATIC_VENTILATION_DRY = 269485312;
    public static final int HVAC_FUNC_AUTO_BLOWING_MODE = 268896000;
    public static final int HVAC_FUNC_AUTO_CLOSE_WINDOW_REMIND = 269418752;
    public static final int HVAC_FUNC_AUTO_CLOSE_WINDOW_REMIND_CONFIRM = 269419264;
    public static final int HVAC_FUNC_AUTO_CLOSE_WINDOW_REMIND_REQUEST = 269419008;
    public static final int HVAC_FUNC_AUTO_CZIS = 269485568;
    public static final int HVAC_FUNC_AUTO_DEFROST_CONFIRM = 268699392;
    public static final int HVAC_FUNC_AUTO_DEFROST_FRONT = 268698880;
    public static final int HVAC_FUNC_AUTO_DEFROST_REAR = 268698624;
    public static final int HVAC_FUNC_AUTO_DEFROST_REQUEST = 268699136;
    public static final int HVAC_FUNC_AUTO_DEHUMIDIFICATION = 268960512;
    public static final int HVAC_FUNC_AUTO_DEHUMIDIFICATION_CONFIRM = 269287936;
    public static final int HVAC_FUNC_AUTO_DEHUMIDIFICATION_REQUEST = 269287680;
    public static final int HVAC_FUNC_AUTO_FAN_SETTING = 268567040;
    public static final int HVAC_FUNC_AUTO_ION = 269222144;
    public static final int HVAC_FUNC_AUTO_ION_CONFIRM = 269222656;
    public static final int HVAC_FUNC_AUTO_ION_REQUEST = 269222400;
    public static final int HVAC_FUNC_AUTO_SEAT_HEATING = 268764416;
    public static final int HVAC_FUNC_AUTO_SEAT_HEATING_TIME = 268764672;
    public static final int HVAC_FUNC_AUTO_SEAT_MASSAGE = 268765184;
    public static final int HVAC_FUNC_AUTO_SEAT_MASSAGE_TIME = 268765440;
    public static final int HVAC_FUNC_AUTO_SEAT_VENTILATION = 268763904;
    public static final int HVAC_FUNC_AUTO_SEAT_VENTILATION_TIME = 268764160;
    public static final int HVAC_FUNC_AUTO_SECOND_ROW_CLIMATE = 269484288;
    public static final int HVAC_FUNC_AUTO_STEERING_WHEEL_HEAT = 269025792;
    public static final int HVAC_FUNC_AUTO_STEERING_WHEEL_HEAT_SWITCH = 269026304;
    public static final int HVAC_FUNC_AUTO_STEERING_WHEEL_HEAT_TIME = 269026048;
    public static final int HVAC_FUNC_BLOWING_MODE = 268894464;
    public static final int HVAC_FUNC_BLOWING_TEMP_COLOR = 268895744;
    public static final int HVAC_FUNC_CIRCULATION = 268632320;
    public static final int HVAC_FUNC_CIRCULATION_LONG_TOUCH = 268632832;
    public static final int HVAC_FUNC_CIRCULATION_TIMER = 268632576;
    public static final int HVAC_FUNC_CLIMATE_HARDKEY_SOUND = 269486080;
    public static final int HVAC_FUNC_CLIMATE_LOCK = 269484544;
    public static final int HVAC_FUNC_CLIMATE_ZONE = 268502272;

    @VendorDefinition(author = "@ECARX", date = "2021-02-04", project = "FY11", requirement = "")
    public static final int HVAC_FUNC_CLIMATISATION_ERROR_CONDITIONS = 269091584;
    public static final int HVAC_FUNC_CO2_HIGHER_CONFIRM = 269353728;
    public static final int HVAC_FUNC_CO2_HIGHER_REQUEST = 269353472;
    public static final int HVAC_FUNC_CO2_SWITCH = 269353216;
    public static final int HVAC_FUNC_DEFROST_FRONT = 268697856;
    public static final int HVAC_FUNC_DEFROST_FRONT_MAX = 268698112;
    public static final int HVAC_FUNC_DEFROST_REAR = 268698368;

    @VendorDefinition(author = "@ECARX", date = "2022-01-12", project = "smart", requirement = "")
    public static final int HVAC_FUNC_DEODORIZATIO_OFF_REASON = 269748240;
    public static final int HVAC_FUNC_DIRECTION_MODE = 268894976;
    public static final int HVAC_FUNC_DISPLAY_WINDOW_TAB = 269484800;
    public static final int HVAC_FUNC_ECO_SWITCH = 268960000;

    @VendorDefinition(author = "@ECARX", date = "2021-04-27", project = "Lambda", requirement = "")
    public static final int HVAC_FUNC_ELECTRICAL_AIR_VENT = 269746432;
    public static final int HVAC_FUNC_FAN_SPEED = 268566784;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "smart")
    public static final int HVAC_FUNC_FILTER_ELEMENT_LIFE = 269746944;

    @VendorDefinition(author = "@ECARX", date = "2022-09-03", project = "smart", requirement = "")
    public static final int HVAC_FUNC_FRONT_NOZZLE_HEATING = 269747200;

    @VendorDefinition(author = "@ECARX", date = "2022-10-19", project = "smart", requirement = "")
    public static final int HVAC_FUNC_FRONT_WINDSHIELD_HEATING = 269747456;

    @VendorDefinition(author = "@ECARX", date = "2022-10-31", project = "smart", requirement = "")
    public static final int HVAC_FUNC_FRONT_WINDSHIELD_HEATING_TIMEOUT = 269747712;
    public static final int HVAC_FUNC_G_CLEAN = 269485056;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "3BE02")
    public static final int HVAC_FUNC_HARDKEY = 269746688;

    @VendorDefinition(author = "@ECARX", date = "2021-12-13", project = "Lambda", requirement = "")
    public static final int HVAC_FUNC_INTELLIGENCE_AC = 269877504;

    @VendorDefinition(author = "@ECARX", date = "2021-01-05", project = "KX11", requirement = "XQ2020091666751")
    public static final int HVAC_FUNC_INTELLIGENT_RECOMMENDATION = 269615360;
    public static final int HVAC_FUNC_IONS_SWITCH = 268961024;

    @VendorDefinition(author = "@ECARX", date = "2021-02-04", project = "FY11", requirement = "")
    public static final int HVAC_FUNC_MODULE_CONNECT_STATUS = 269680896;
    public static final int HVAC_FUNC_OVERHEAT_PROTECTION = 268960768;

    @VendorDefinition(author = "@ECARX", date = "2021-07-27", project = "Lambda", requirement = "")
    public static final int HVAC_FUNC_PM25_SWITCH = 269811968;
    public static final int HVAC_FUNC_POST_CLIMATISATION = 269091328;
    public static final int HVAC_FUNC_POWER = 268501248;
    public static final int HVAC_FUNC_POWER_VR = 268505344;
    public static final int HVAC_FUNC_PRE_CLIMATISATION = 269091072;
    public static final int HVAC_FUNC_SEAT_HEATING = 268763648;
    public static final int HVAC_FUNC_SEAT_MASSAGE = 268764928;
    public static final int HVAC_FUNC_SEAT_MASSAGE_PROGRAM = 268765952;
    public static final int HVAC_FUNC_SEAT_MASSAGE_SWITCH = 268765696;
    public static final int HVAC_FUNC_SEAT_VENTILATION = 268763392;
    public static final int HVAC_FUNC_SECOND_ROW_SEAT_HEATING = 268766208;
    public static final int HVAC_FUNC_STEERING_WHEEL_HEAT = 269025536;
    public static final int HVAC_FUNC_SWEEPING_HORIZONTAL_POS = 268895232;
    public static final int HVAC_FUNC_SWEEPING_MODE = 268894720;
    public static final int HVAC_FUNC_SWEEPING_VERTICAL_POS = 268895488;
    public static final int HVAC_FUNC_TEMP = 268828928;
    public static final int HVAC_FUNC_TEMP_DUAL = 268829952;
    public static final int HVAC_FUNC_TEMP_MAX = 268829184;
    public static final int HVAC_FUNC_TEMP_MIN = 268829440;

    @VendorDefinition(author = "@ECARX", date = "2021-01-05", project = "KX11", requirement = "XQ2020091666751")
    public static final int HVAC_FUNC_TEMP_OPTIMIZE = 269615616;
    public static final int HVAC_FUNC_TEMP_STEP = 268829696;
    public static final int HVAC_FUNC_TEMP_UNIT = 268830208;
    public static final int HVAC_FUNC_VENTILATION_ONTIME = 269485824;
    public static final int HVAC_HARDKEY_AC = 269746695;
    public static final int HVAC_HARDKEY_AC_MAX = 269746699;
    public static final int HVAC_HARDKEY_AUTO = 269746694;
    public static final int HVAC_HARDKEY_FAN_DOWN = 269746692;
    public static final int HVAC_HARDKEY_FAN_UP = 269746691;
    public static final int HVAC_HARDKEY_FRONT_DEFROST = 269746697;
    public static final int HVAC_HARDKEY_LEFT_TEMP = 269746689;
    public static final int HVAC_HARDKEY_LOOP = 269746696;
    public static final int HVAC_HARDKEY_MODE = 269746693;
    public static final int HVAC_HARDKEY_NO_TOUCH = 269746688;
    public static final int HVAC_HARDKEY_REAR_DEFROST = 269746698;
    public static final int HVAC_HARDKEY_RIGHT_TEMP = 269746690;
    public static final int HVAC_HARDKEY_TEMP_SYNC = 269746699;
    public static final int PM25_TPYE_STATE_COLLECTING = 269811970;
    public static final int PM25_TPYE_STATE_COMPLETED = 269811972;
    public static final int PM25_TPYE_STATE_INITIAL = 269811969;
    public static final int PM25_TPYE_STATE_OFF = 269811971;
    public static final int PM25_TPYE_STATE_UNKNOW = -1;
    public static final int SCENE_FUNC_DEODORIZATIO_OFF_BY_MAX_AC = 269748241;
    public static final int SCENE_FUNC_DEODORIZATIO_OFF_BY_MAX_DEF = 269748242;
    public static final int SCENE_FUNC_DEODORIZATIO_OFF_BY_TIME_OUT = 269748244;
    public static final int SCENE_FUNC_DEODORIZATIO_OFF_BY_VENT = 269748243;
    public static final int SCENE_FUNC_DEODORIZATIO_OFF_BY_VMM_ENABLE = 269748245;
    public static final int SEAT_HEATING_LEVEL_1 = 268763649;
    public static final int SEAT_HEATING_LEVEL_2 = 268763650;
    public static final int SEAT_HEATING_LEVEL_3 = 268763651;
    public static final int SEAT_HEATING_LEVEL_AUTO = 268763663;
    public static final int SEAT_HEATING_OFF = 0;
    public static final int SEAT_HEATING_TIMER_OUT_OFF = 268763652;
    public static final int SEAT_MASSAGE_LEVEL_1 = 268764929;
    public static final int SEAT_MASSAGE_LEVEL_2 = 268764930;
    public static final int SEAT_MASSAGE_LEVEL_3 = 268764931;
    public static final int SEAT_MASSAGE_LEVEL_AUTO = 268764943;
    public static final int SEAT_MASSAGE_OFF = 0;
    public static final int SEAT_MASSAGE_PROGRAM_1 = 268765953;
    public static final int SEAT_MASSAGE_PROGRAM_2 = 268765954;
    public static final int SEAT_MASSAGE_PROGRAM_3 = 268765955;
    public static final int SEAT_MASSAGE_PROGRAM_4 = 268765956;
    public static final int SEAT_MASSAGE_PROGRAM_5 = 268765957;
    public static final int SEAT_MASSAGE_PROGRAM_6 = 268765958;
    public static final int SEAT_MASSAGE_PROGRAM_7 = 268765959;
    public static final int SEAT_MASSAGE_PROGRAM_8 = 268765960;
    public static final int SEAT_MASSAGE_PROGRAM_9 = 268765961;
    public static final int SEAT_MASSAGE_PROGRAM_A = 268765962;
    public static final int SEAT_MASSAGE_PROGRAM_OFF = 0;
    public static final int SEAT_VENTILATION_LEVEL_1 = 268763393;
    public static final int SEAT_VENTILATION_LEVEL_2 = 268763394;
    public static final int SEAT_VENTILATION_LEVEL_3 = 268763395;
    public static final int SEAT_VENTILATION_LEVEL_AUTO = 268763407;
    public static final int SEAT_VENTILATION_OFF = 0;
    public static final int STEERING_WHEEL_HEAT_AUTO = 269025551;
    public static final int STEERING_WHEEL_HEAT_HIGH = 269025539;
    public static final int STEERING_WHEEL_HEAT_LOW = 269025537;
    public static final int STEERING_WHEEL_HEAT_MID = 269025538;
    public static final int STEERING_WHEEL_HEAT_OFF = 0;
    public static final int SWEEPING_MODE_LEFT_RIGHT = 268894721;
    public static final int SWEEPING_MODE_LR_AND_UD = 268894723;
    public static final int SWEEPING_MODE_OFF = 0;
    public static final int SWEEPING_MODE_UP_DOWN = 268894722;
    public static final int TEMPERATURE_UNIT_C = 268830209;
    public static final int TEMPERATURE_UNIT_F = 268830210;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AirFragranceLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AirFragranceType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoFanSetting {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoSeatHeatingLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoSeatHeatingTime {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoSeatMassageLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoSeatMassageTime {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoSeatVentilationLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoSeatVentilationTime {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoSteeringWheelHeatLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoSteeringWheelHeatTime {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BlowingMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CirculationMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ClimateZone {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DeodorateOffReasonMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DirectionMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DisplayWindowTab {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FanSpeedLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FrontWindshieldHeatingTimeoutState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface HvacFunction {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "3BE02")
    @Retention(RetentionPolicy.SOURCE)
    public @interface HvacHardKeyValue {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Pm25SwitchState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeatHeatingLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeatMassageLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeatMassageProgram {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeatVentilationLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SteeringWheelHeatLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SweepingMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TemperatureUnit {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface afuswitchrespSignalLost {
    }
}
