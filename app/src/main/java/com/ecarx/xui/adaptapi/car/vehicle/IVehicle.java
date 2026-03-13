package com.ecarx.xui.adaptapi.car.vehicle;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IVehicle {
    public static final int ABNORMAL_VEHICLE_ALARM_OFF = 539492609;
    public static final int ABNORMAL_VEHICLE_ALARM_SOUND = 539492611;
    public static final int ABNORMAL_VEHICLE_ALARM_VISUAL_SOUND = 539492610;
    public static final int AMBIENCE_LIGHT_EXPERIENCE_CUSTOM = 537526530;
    public static final int AMBIENCE_LIGHT_EXPERIENCE_FULL = 537526529;
    public static final int AMBIENCE_LIGHT_MAINCOLOR_BREATHE_MODE = 537526790;
    public static final int AMBIENCE_LIGHT_MAINCOLOR_DRIVERMODE = 537526786;
    public static final int AMBIENCE_LIGHT_MAINCOLOR_MUSIC = 537526788;
    public static final int AMBIENCE_LIGHT_MAINCOLOR_NONE = 0;
    public static final int AMBIENCE_LIGHT_MAINCOLOR_NON_POLAR = 537526789;
    public static final int AMBIENCE_LIGHT_MAINCOLOR_SETCOLOR = 537526787;
    public static final int AMBIENCE_LIGHT_MAINCOLOR_SPEED_MODE = 537526791;

    @VendorDefinition(author = "@ECARX", date = "2020-11-25", project = "ALL")
    public static final int AMBIENCE_LIGHT_MAINCOLOR_TEMPERATURE_MODE = 537526793;
    public static final int AMBIENCE_LIGHT_MAINCOLOR_THEME = 537526785;
    public static final int AMBIENCE_LIGHT_MAINCOLOR_WEATHER = 537526792;
    public static final int ARTIFICIAL_SOUND_TYPE_1 = 538575873;
    public static final int ARTIFICIAL_SOUND_TYPE_2 = 538575874;
    public static final int ARTIFICIAL_SOUND_TYPE_3 = 538575875;
    public static final int ARTIFICIAL_SOUND_TYPE_4 = 538575876;
    public static final int ARTIFICIAL_SOUND_TYPE_5 = 538575877;
    public static final int ARTIFICIAL_SOUND_TYPE_6 = 538575878;
    public static final int ARTIFICIAL_SOUND_TYPE_7 = 538575879;
    public static final int ARTIFICIAL_SOUND_TYPE_8 = 538575880;
    public static final int ARTIFICIAL_SOUND_TYPE_NONE = 0;
    public static final int AUTO_CLOSE_WINDOW_KEY_LONG_PRESS = 537396226;
    public static final int AUTO_CLOSE_WINDOW_OFF = 0;
    public static final int AUTO_CLOSE_WINDOW_VEHICLE_LOCK = 537396225;
    public static final int BRIGHTNESS_DRIVING_HIGH = 537528066;
    public static final int BRIGHTNESS_DRIVING_LOW = 537528065;
    public static final int BRIGHTNESS_DRIVING_OFF = 0;
    public static final int BRIGHTNESS_STATIONARY_HIGH = 537527810;
    public static final int BRIGHTNESS_STATIONARY_LOW = 537527809;
    public static final int BRIGHTNESS_STATIONARY_OFF = 0;
    public static final int CAR_LOCATOR_REMINDER_MODE_LIGHT = 538313730;
    public static final int CAR_LOCATOR_REMINDER_MODE_LIGHT_SOUND = 538313731;
    public static final int CAR_LOCATOR_REMINDER_MODE_OFF = 0;
    public static final int CAR_LOCATOR_REMINDER_MODE_SOUND = 538313729;
    public static final int CONGESTION_AHEAD_OFF = 539493377;
    public static final int CONGESTION_AHEAD_SOUND = 539493379;
    public static final int CONGESTION_AHEAD_VISUAL_SOUND = 539493378;
    public static final int DANGEROUS_ROAD_OFF = 539492865;
    public static final int DANGEROUS_ROAD_SOUND = 539492867;
    public static final int DANGEROUS_ROAD_VISUAL_SOUND = 539492866;
    public static final int DAYMODE_SETTING_AUTO = 538247427;
    public static final int DAYMODE_SETTING_DAY = 538247425;
    public static final int DAYMODE_SETTING_NIGHT = 538247426;
    public static final int DAYMODE_SETTING_OFF = 0;
    public static final int DOOR_OPEN_WARN_VOLUME_HIGH = 538051075;
    public static final int DOOR_OPEN_WARN_VOLUME_LOW = 538051073;
    public static final int DOOR_OPEN_WARN_VOLUME_MID = 538051074;
    public static final int DOOR_OPEN_WARN_VOLUME_OFF = 0;
    public static final int EMERGENCY_VEHICLE_OFF = 539493121;
    public static final int EMERGENCY_VEHICLE_SOUND = 539493123;
    public static final int EMERGENCY_VEHICLE_VISUAL_SOUND = 539493122;
    public static final int ENERGY_REGENERATION_LEVEL_HIGH = 537003267;
    public static final int ENERGY_REGENERATION_LEVEL_LOW = 537003265;
    public static final int ENERGY_REGENERATION_LEVEL_MID = 537003266;
    public static final int ENERGY_REGENERATION_LEVEL_OFF = 0;
    public static final int ESC_AVH_SWITCH_AVAILABLE_STATE = 537265153;
    public static final int ESC_HDC_SWITCH_AVAILABLE_STATE = 537265409;
    public static final int ESM_VOLUME_LEVEL_HIGH = 538575363;
    public static final int ESM_VOLUME_LEVEL_LOW = 538575361;
    public static final int ESM_VOLUME_LEVEL_MID = 538575362;
    public static final int ESM_VOLUME_LEVEL_OFF = 0;
    public static final int FESTIVAL_TYPE_CHRISTMAS = 539496201;
    public static final int FESTIVAL_TYPE_DRAGON_BOAT_FESTIVAL = 539496197;
    public static final int FESTIVAL_TYPE_LABOR_DAY = 539496196;
    public static final int FESTIVAL_TYPE_MID_AUTUMN_FESTIVAL = 539496199;
    public static final int FESTIVAL_TYPE_NATIONAL_DAY = 539496200;
    public static final int FESTIVAL_TYPE_NEW_YEAR = 539496193;
    public static final int FESTIVAL_TYPE_NULL = 539496192;
    public static final int FESTIVAL_TYPE_SPRING_FESTIVAL = 539496194;
    public static final int FESTIVAL_TYPE_TANABATA_FESTIVAL = 539496198;
    public static final int FESTIVAL_TYPE_VALENTINE_DAY = 539496195;
    public static final int FORWARD_COLLISION_WARN_SNVTY_HIGH = 537788931;
    public static final int FORWARD_COLLISION_WARN_SNVTY_LOW = 537788929;
    public static final int FORWARD_COLLISION_WARN_SNVTY_NORMAL = 537788930;
    public static final int FORWARD_COLLISION_WARN_SNVTY_OFF = 0;
    public static final int HMI_THEMES_VALUE_CLEAR = 536936705;
    public static final int HMI_THEMES_VALUE_HYPER = 536936708;
    public static final int HMI_THEMES_VALUE_INTER = 536936707;
    public static final int HMI_THEMES_VALUE_LOUDER = 536936706;
    public static final int HOME_SAFE_LIGHT_VALUE_30S = 537134849;
    public static final int HOME_SAFE_LIGHT_VALUE_60S = 537134850;
    public static final int HOME_SAFE_LIGHT_VALUE_90S = 537134851;
    public static final int HOME_SAFE_LIGHT_VALUE_OFF = 0;
    public static final int KEYLESS_UNLOCKING_ALL_DOORS = 537920513;
    public static final int KEYLESS_UNLOCKING_OFF = 0;
    public static final int KEYLESS_UNLOCKING_SINGLE_DOOR = 537920514;
    public static final int LAMP_AUTOLIGHT_VALUE_EARLIER = 537133827;
    public static final int LAMP_AUTOLIGHT_VALUE_LATER = 537133825;
    public static final int LAMP_AUTOLIGHT_VALUE_NORMAL = 537133826;
    public static final int LAMP_EXTERIOR_LIGHT_CONTROL_AHBC = 537136644;
    public static final int LAMP_EXTERIOR_LIGHT_CONTROL_AUTOMATIC = 537136643;
    public static final int LAMP_EXTERIOR_LIGHT_CONTROL_LOWBEAM = 537136642;
    public static final int LAMP_EXTERIOR_LIGHT_CONTROL_OFF = 0;
    public static final int LAMP_EXTERIOR_LIGHT_CONTROL_POS_LIGHT = 537136641;
    public static final int LAMP_HIGHBEAM_ACTIVE_VALUE_FAST = 537133571;
    public static final int LAMP_HIGHBEAM_ACTIVE_VALUE_NORMAL = 537133570;
    public static final int LAMP_HIGHBEAM_ACTIVE_VALUE_SLOW = 537133569;
    public static final int LANE_CHANGE_WARNING_SOUND_HIGH = 537330691;
    public static final int LANE_CHANGE_WARNING_SOUND_LOW = 537330689;
    public static final int LANE_CHANGE_WARNING_SOUND_MID = 537330690;
    public static final int LANE_CHANGE_WARNING_SOUND_OFF = 0;
    public static final int LANE_KEEPING_AID_MODE_INTV = 537330178;
    public static final int LANE_KEEPING_AID_MODE_OFF = 0;
    public static final int LANE_KEEPING_AID_MODE_WARN = 537330179;
    public static final int LANE_KEEPING_AID_MODE_WARN_INTV = 537330177;
    public static final int LANE_KEEPING_AID_WARNING_HAPTIC = 537330946;
    public static final int LANE_KEEPING_AID_WARNING_OFF = 0;
    public static final int LANE_KEEPING_AID_WARNING_SOUND = 537330945;
    public static final int LANE_KEEPING_AID_WARNING_SOUND_HAPTIC = 537330947;
    public static final int LAUNCH_MODE_BATTERY_POWER_LOW = 1;
    public static final int LAUNCH_MODE_BATTERY_TEMPER_HIGHER = 2;
    public static final int LAUNCH_MODE_BATTERY_TEMPER_LOWER = 3;
    public static final int LAUNCH_MODE_BRAKE_PEDAL_RELEASE_OVERTIME = 11;
    public static final int LAUNCH_MODE_DISABLE = 0;
    public static final int LAUNCH_MODE_ESC_NOT_TURN_OFF = 4;
    public static final int LAUNCH_MODE_NO_CANCEL_TRAILER = 5;
    public static final int LAUNCH_MODE_NO_CLOSE_DOORS = 9;
    public static final int LAUNCH_MODE_NO_CLOSE_DRIVER_BELT = 8;
    public static final int LAUNCH_MODE_NO_RELEASE_EPB = 7;
    public static final int LAUNCH_MODE_NO_STRAIGHT_STEERING_WHEEL = 6;
    public static final int LAUNCH_MODE_STANDBY_OVERTIME = 10;
    public static final int LAUNCH_MODE_USE_FREQUENCY_TOO_FASE = 12;
    public static final int MIRROR_DIMMING_DARK = 537460993;
    public static final int MIRROR_DIMMING_LIGHT = 537460995;
    public static final int MIRROR_DIMMING_NORMAL = 537460994;
    public static final int MIRROR_DIMMING_OFF = 0;
    public static final int MIRROR_DIPPING_BOTH = 537461507;
    public static final int MIRROR_DIPPING_DRIVER = 537461505;
    public static final int MIRROR_DIPPING_OFF = 0;
    public static final int MIRROR_DIPPING_PASSENGER = 537461506;
    public static final int PARKING_COMFORT_MODE_TIMER_0 = 0;
    public static final int PARKING_COMFORT_MODE_TIMER_120 = 538640646;
    public static final int PARKING_COMFORT_MODE_TIMER_15 = 538640641;
    public static final int PARKING_COMFORT_MODE_TIMER_30 = 538640642;
    public static final int PARKING_COMFORT_MODE_TIMER_45 = 538640643;
    public static final int PARKING_COMFORT_MODE_TIMER_60 = 538640644;
    public static final int PARKING_COMFORT_MODE_TIMER_90 = 538640645;
    public static final int PARK_ASSIST_SYS_VOLUME_HIGH = 537723395;
    public static final int PARK_ASSIST_SYS_VOLUME_LOW = 537723393;
    public static final int PARK_ASSIST_SYS_VOLUME_MID = 537723394;
    public static final int PARK_ASSIST_SYS_VOLUME_OFF = 0;
    public static final int PEB_MODE_MSP = 537264642;
    public static final int PEB_MODE_OFF = 0;
    public static final int PEB_MODE_PEB = 537264641;
    public static final int PRIVATE_LOCK_REMINDER_OFF = 0;
    public static final int PRIVATE_LOCK_REMINDER_ON = 1;
    public static final int PRIVATE_LOCK_REMINDER_REMINDER = 537854465;
    public static final int REAR_SPOILER_ADJUST_OFF = 0;
    public static final int REAR_SPOILER_ADJUST_P1 = 538510337;
    public static final int REAR_SPOILER_ADJUST_P2 = 538510338;
    public static final int RED_LIGHT_OFF = 539493889;
    public static final int RED_LIGHT_SOUND = 539493891;
    public static final int RED_LIGHT_VISUAL_SOUND = 539493890;
    public static final int REMOTE_UNLOCKING_ALL_DOORS = 537920769;
    public static final int REMOTE_UNLOCKING_OFF = 0;
    public static final int REMOTE_UNLOCKING_SINGLE_DOOR = 537920770;
    public static final int ROTATED_WHEELS_WARNING_INFO_LEFTWARD = 538772225;
    public static final int ROTATED_WHEELS_WARNING_INFO_NONE = 0;
    public static final int ROTATED_WHEELS_WARNING_INFO_RIGHTWARD = 538772226;

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    public static final int SETTING_FUNC_ABNORMAL_VEHICLE_ALARM = 539492608;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_BOTZONES = 537527040;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_BRIGHTNESS_DRIVING = 537528064;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_BRIGHTNESS_STATIONARY = 537527808;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_COLOR_SET = 537528576;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_EXPERIENCE = 537526528;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_INTERACTIVE_EFFECT = 537528320;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_MAINCOLOR = 537526784;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_MAINZONES = 537527552;
    public static final int SETTING_FUNC_AMBIENCE_LIGHT_TOPZONES = 537527296;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "smart", requirement = "")
    public static final int SETTING_FUNC_ARTIFICIAL_SOUND_PREVIEW = 539428608;
    public static final int SETTING_FUNC_ARTIFICIAL_SOUND_SWITCH = 538575616;
    public static final int SETTING_FUNC_ARTIFICIAL_SOUND_TYPE = 538575872;
    public static final int SETTING_FUNC_AUDIBLE_LOCKING_FEEDBACK = 537920256;
    public static final int SETTING_FUNC_AUTONOMOUS_EMERGENCY_BRAKING = 537333248;
    public static final int SETTING_FUNC_AUTO_CLOSE_ROOF_RAINY = 537395968;
    public static final int SETTING_FUNC_AUTO_CLOSE_WINDOW = 537396224;
    public static final int SETTING_FUNC_AUTO_HOLD = 537265152;
    public static final int SETTING_FUNC_AUTO_REAR_WIPING = 537657856;
    public static final int SETTING_FUNC_AUTO_TRAILER_LAMP_CHECK = 537135872;
    public static final int SETTING_FUNC_AVAS_SOUND_TYPE = 538576640;
    public static final int SETTING_FUNC_AVAS_SWITCH = 538576128;
    public static final int SETTING_FUNC_AVAS_VOLUME = 538576384;
    public static final int SETTING_FUNC_BLIND_CAMERA_SYNC_RT_TURN = 538772480;
    public static final int SETTING_FUNC_BRIGHTNESS_DAY = 538247936;
    public static final int SETTING_FUNC_BRIGHTNESS_MAX = 538248448;
    public static final int SETTING_FUNC_BRIGHTNESS_MIN = 538248704;
    public static final int SETTING_FUNC_BRIGHTNESS_NIGHT = 538248192;
    public static final int SETTING_FUNC_BRIGHTNESS_STEP = 538248960;
    public static final int SETTING_FUNC_CAR_LOCATOR = 538312960;
    public static final int SETTING_FUNC_CAR_LOCATOR_REMINDER_MODE = 538313728;
    public static final int SETTING_FUNC_CENTRAL_LOCK = 537921792;

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    public static final int SETTING_FUNC_CONGESTION_AHEAD_ALARM = 539493376;

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    public static final int SETTING_FUNC_DANGEROUS_ROAD_ALARM = 539492864;

    @VendorDefinition(author = "@ECARX", date = "2021-05-17", project = "SMART", requirement = "")
    public static final int SETTING_FUNC_DATA_COLLECTION = 539361792;
    public static final int SETTING_FUNC_DAYLIGHT_SAVING_TIME = 538640896;
    public static final int SETTING_FUNC_DAYMODE_SETTING = 538247424;
    public static final int SETTING_FUNC_DAYMODE_SYNC = 538247680;

    @VendorDefinition(author = "@ECARX", date = "2021-09-13", project = "lambda", requirement = "")
    public static final int SETTING_FUNC_DIGITAL_KEY = 539496448;
    public static final int SETTING_FUNC_DIGITAL_KEY_REQ_STS = 539496704;
    public static final int SETTING_FUNC_DIGITAL_KEY_SUSPENSION = 539497472;
    public static final int SETTING_FUNC_DIGITAL_KEY_TERMINATION = 539497216;
    public static final int SETTING_FUNC_DIGITAL_KEY_UNPAIR = 539496960;

    @VendorDefinition(author = "@ECARX", date = "2021-2-26", project = "KX11")
    public static final int SETTING_FUNC_DIM_HOLIDAY_WALLPAPER = 538904320;
    public static final int SETTING_FUNC_DOOR_OPEN_WARN_ACTIVE = 538050816;
    public static final int SETTING_FUNC_DOOR_OPEN_WARN_VOLUME = 538051072;
    public static final int SETTING_FUNC_DRIVER_ALERT_CONTROL = 537002496;
    public static final int SETTING_FUNC_DRIVER_PERFOR_SUPPORT = 537003520;
    public static final int SETTING_FUNC_EASY_INGRESS_EGRESS = 538378496;

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    public static final int SETTING_FUNC_ELECTRIC_MILEAGE_DISPLAY_SWITCH = 539429632;
    public static final int SETTING_FUNC_ELE_SEATBELT_COMFORT = 537333504;

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    public static final int SETTING_FUNC_EMERGENCY_VEHICLE_ALARM = 539493120;
    public static final int SETTING_FUNC_EMGY_LANE_KEEP_AID = 537331200;
    public static final int SETTING_FUNC_EMGY_LANE_OCC_WARNING = 537332480;

    @VendorDefinition(author = "@ECARX", date = "2020-10-31", project = "ALL")
    public static final int SETTING_FUNC_ENERGY_PREDICTION_SWITCH = 538903808;
    public static final int SETTING_FUNC_ENERGY_REGENERATION = 537003264;
    public static final int SETTING_FUNC_ENGINE_STOP_START = 537002240;
    public static final int SETTING_FUNC_ESC_SPORT_MODE = 537002752;
    public static final int SETTING_FUNC_ESM_SWITCH = 538575104;
    public static final int SETTING_FUNC_ESM_VOLUME = 538575360;
    public static final int SETTING_FUNC_EVASIVE_MANEUVER_AID = 537332736;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "Lambda")
    public static final int SETTING_FUNC_EXTERNAL_ARTIFICIAL_SOUND_SWITCH = 538576896;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "Lambda")
    public static final int SETTING_FUNC_EXTERNAL_ARTIFICIAL_SOUND_TYPE = 538577152;

    @VendorDefinition(author = "@ECARX", date = "2021-06-01", project = "smart", requirement = "")
    public static final int SETTING_FUNC_EYE_BALL_TRACK = 539427584;
    public static final int SETTING_FUNC_E_PEDAL = 538444032;
    public static final int SETTING_FUNC_FACIAL_RECOGNITION = 538706432;
    public static final int SETTING_FUNC_FORWARD_COLLISION_WARN = 537788672;
    public static final int SETTING_FUNC_FORWARD_COLLISION_WARN_SNVTY = 537788928;
    public static final int SETTING_FUNC_FRONT_CROSS_TRAFFIC_ALERT = 537331968;
    public static final int SETTING_FUNC_FRONT_WIPER_IDLE = 537658112;
    public static final int SETTING_FUNC_HDC_SWITCH = 537265408;

    @VendorDefinition(author = "@ECARX", date = "2021-07-02", project = "lambda", requirement = "")
    public static final int SETTING_FUNC_HILL_START_ASSIST = 539429376;
    public static final int SETTING_FUNC_HMI_THEMES_CHANGE = 536936704;
    public static final int SETTING_FUNC_HUD_ACTIVE = 537985280;
    public static final int SETTING_FUNC_HUD_CALIBRATION = 537985536;

    @VendorDefinition(author = "@ECARX", date = "2021-2-19", project = "KX11")
    public static final int SETTING_FUNC_INTELLIGENT_FUEL_SAVE = 538904064;
    public static final int SETTING_FUNC_INTERNAL_COMMUNICATION = 538902784;
    public static final int SETTING_FUNC_INTERNAL_COMMUNICATION_VOLUME = 538903040;
    public static final int SETTING_FUNC_JOURNAL_LOGS = 538313472;
    public static final int SETTING_FUNC_KEYLESS_UNLOCKING = 537920512;
    public static final int SETTING_FUNC_LAMP_ADAPTIVE_FRONT_LIGHT = 537136384;
    public static final int SETTING_FUNC_LAMP_APPROACH_LIGHT = 537135360;
    public static final int SETTING_FUNC_LAMP_AUTOLIGHT = 537133824;
    public static final int SETTING_FUNC_LAMP_AUTOMATIC_COURTESY_LIGHT = 537134592;
    public static final int SETTING_FUNC_LAMP_AUX_HIGHBEAM = 537134080;
    public static final int SETTING_FUNC_LAMP_BENDINGLIGHT = 537134336;
    public static final int SETTING_FUNC_LAMP_CORNERING_LIGHT = 537135616;
    public static final int SETTING_FUNC_LAMP_DAYTIME_RUNNING_LIGHT = 537135104;
    public static final int SETTING_FUNC_LAMP_EXTERIOR_LIGHT_CONTROL = 537136640;
    public static final int SETTING_FUNC_LAMP_HIGHBEAM_ACTIVE = 537133568;
    public static final int SETTING_FUNC_LAMP_HOME_SAFE_LIGHT = 537134848;
    public static final int SETTING_FUNC_LAMP_TRIPLE_FLASH = 537133312;
    public static final int SETTING_FUNC_LANE_CHANGE_ASSIST = 537331456;
    public static final int SETTING_FUNC_LANE_CHANGE_WARING = 537330432;
    public static final int SETTING_FUNC_LANE_CHANGE_WARNING_SOUND = 537330688;
    public static final int SETTING_FUNC_LANE_KEEPING_AID = 537329920;
    public static final int SETTING_FUNC_LANE_KEEPING_AID_MODE = 537330176;
    public static final int SETTING_FUNC_LANE_KEEPING_AID_WARNING = 537330944;

    @VendorDefinition(author = "@ECARX", date = "2021-07-02", project = "lambda", requirement = "")
    public static final int SETTING_FUNC_LAUNCH_MODE = 539428864;

    @VendorDefinition(author = "@ECARX", date = "2021-07-02", project = "lambda", requirement = "")
    public static final int SETTING_FUNC_LAUNCH_MODE_INDCN = 539429120;

    @VendorDefinition(author = "@ECARX", date = "2021-06-01", project = "smart", requirement = "")
    public static final int SETTING_FUNC_LIFE_DETECTION = 539427328;
    public static final int SETTING_FUNC_LOCK_REAR_SEAT_DISPLAY = 538706176;
    public static final int SETTING_FUNC_LOCK_RFDM_REMOTE_CONTROLLER = 538707968;
    public static final int SETTING_FUNC_MAINTENANCE_MILEAGE_RESET = 538968320;
    public static final int SETTING_FUNC_MAINTENANCE_TIME_RESET = 538968576;
    public static final int SETTING_FUNC_MANUAL_TRAILER_LAMP_CHECK = 537136128;
    public static final int SETTING_FUNC_MIRROR_AUTO_FOLDING = 537461248;
    public static final int SETTING_FUNC_MIRROR_DIMMING = 537460992;
    public static final int SETTING_FUNC_MIRROR_DIPPING = 537461504;

    @VendorDefinition(author = "@ECARX", date = "2021-5-7", project = "Lambda")
    public static final int SETTING_FUNC_MIRROR_DIPPING_SWITCH = 537461760;

    @VendorDefinition(author = "@ECARX", date = "2021-07-13", project = "smart", requirement = "")
    public static final int SETTING_FUNC_MULTIMEDIA_GESTURE = 539494144;

    @VendorDefinition(author = "@ECARX", date = "2021-07-13", project = "smart", requirement = "")
    public static final int SETTING_FUNC_MUSIC_GESTURE = 539494656;
    public static final int SETTING_FUNC_PARK_ASSIST_SYS_ACTIVATED = 537723136;
    public static final int SETTING_FUNC_PARK_ASSIST_SYS_VOLUME = 537723392;

    @VendorDefinition(author = "@ECARX", date = "2022-01-12", project = "smart", requirement = "")
    public static final int SETTING_FUNC_PARK_COMFORT_MODE_OFF_REASON = 538838272;
    public static final int SETTING_FUNC_PARK_COMFORT_MODE_TIMER = 538837248;
    public static final int SETTING_FUNC_PARK_COMFORT_MODE_TIMER_MAX = 538837504;
    public static final int SETTING_FUNC_PARK_COMFORT_MODE_TIMER_MIN = 538837760;
    public static final int SETTING_FUNC_PARK_COMFORT_MODE_TIMER_STEP = 538838016;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "Lambda", requirement = "")
    public static final int SETTING_FUNC_PASSENGER_AIRBAG = 539428096;
    public static final int SETTING_FUNC_PASSIVE_ARMING = 537921280;
    public static final int SETTING_FUNC_PBC_AUTO_APPLY = 537264384;

    @VendorDefinition(author = "@ECARX", date = "2021-07-23", project = "smart", requirement = "")
    public static final int SETTING_FUNC_PBC_DOUBLE_EPB_SWITCH = 537268480;
    public static final int SETTING_FUNC_PBC_EPB_SWITCH = 537268224;
    public static final int SETTING_FUNC_PCM_TIMER = 538640640;
    public static final int SETTING_FUNC_PDC_SWITCH = 537264896;
    public static final int SETTING_FUNC_PEB_MODE = 537264640;
    public static final int SETTING_FUNC_PRIVATE_LOCK = 537854208;
    public static final int SETTING_FUNC_PRIVATE_LOCK_REMINDER = 537854464;
    public static final int SETTING_FUNC_REAR_COLLISION_WARNING = 537333760;
    public static final int SETTING_FUNC_REAR_CROSS_TRAFFIC_ALERT = 537332224;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "Lambda")
    public static final int SETTING_FUNC_REAR_SPOILER_ADJUST = 538510336;
    public static final int SETTING_FUNC_REAR_WINDOW_CLEAN = 537395712;
    public static final int SETTING_FUNC_REDUCED_GUARD = 537921536;

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    public static final int SETTING_FUNC_RED_LIGHT_ALARM = 539493888;

    @VendorDefinition(author = "@ECARX", date = "2021-05-17", project = "SMART", requirement = "")
    public static final int SETTING_FUNC_REMOTE_DIAGNOSTICS = 539362048;
    public static final int SETTING_FUNC_REMOTE_UNLOCKING = 537920768;
    public static final int SETTING_FUNC_RESET_SETTINGS_DEFAULT = 538181888;
    public static final int SETTING_FUNC_RMS_ACTIVE = 538116352;
    public static final int SETTING_FUNC_ROTATED_WHEELS_WARNING = 538771968;
    public static final int SETTING_FUNC_ROTATED_WHEELS_WARNING_INFO = 538772224;

    @VendorDefinition(author = "@ECARX", date = "2021-05-17", project = "SMART", requirement = "")
    public static final int SETTING_FUNC_RVDC = 539361536;
    public static final int SETTING_FUNC_SAILING_MODE = 537003008;

    @VendorDefinition(author = "@ECARX", date = "2021-06-01", project = "smart", requirement = "")
    public static final int SETTING_FUNC_SAY_HI = 539427840;
    public static final int SETTING_FUNC_SCREEN_SAVER_CUSTOM_NAME = 539034624;
    public static final int SETTING_FUNC_SCREEN_SAVER_CUSTOM_PICTURE = 539035136;
    public static final int SETTING_FUNC_SCREEN_SAVER_CUSTOM_TEXT = 539034880;
    public static final int SETTING_FUNC_SCREEN_SAVER_STYLE = 539034368;
    public static final int SETTING_FUNC_SOUND_WARNING_VOLUME = 538771712;

    @Deprecated
    public static final int SETTING_FUNC_SPEED_CONTROL = 537068032;
    public static final int SETTING_FUNC_SPEED_CONTROL_MODE = 537069056;

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    public static final int SETTING_FUNC_SPEED_GUIDANCE_ALARM = 539493632;

    @Deprecated
    public static final int SETTING_FUNC_SPEED_LIMITATION = 537067776;
    public static final int SETTING_FUNC_SPEED_LIMITATION_MODE = 537068800;
    public static final int SETTING_FUNC_SPEED_LOCKING = 537921024;
    public static final int SETTING_FUNC_STEERING_ASSISTANCE_LEVEL = 537331712;
    public static final int SETTING_FUNC_SUNROOF_TRANSPARENCY_AUTO = 537396992;
    public static final int SETTING_FUNC_SUSPENSION_DEACTIVATION_DAMPENING = 538509824;
    public static final int SETTING_FUNC_SUSPENSION_DRIVER_ENTRY_CONTROL = 538510080;
    public static final int SETTING_FUNC_SUSPENSION_HEIGHT_ADJUST = 538509568;
    public static final int SETTING_FUNC_TCAM_RESET = 538314240;
    public static final int SETTING_FUNC_TEM_PROVISIONING_STATE = 538313984;

    @VendorDefinition(author = "@ECARX", date = "2021-07-13", project = "smart", requirement = "")
    public static final int SETTING_FUNC_TRACK_GESTURE = 539494400;

    @VendorDefinition(author = "@ECARX", date = "2021-3-16", project = "E03")
    public static final int SETTING_FUNC_TRACK_MODE = 538904576;
    public static final int SETTING_FUNC_TRAFFIC_LIGHT_ATTENTION = 537332992;
    public static final int SETTING_FUNC_TRAFFIC_SIGN_RECOGNITION = 537592064;
    public static final int SETTING_FUNC_TRAFFIC_SIGN_RECOGNITION_ALERT = 537592320;

    @VendorDefinition(author = "@ECARX", date = "2021-07-23", project = "smart", requirement = "")
    public static final int SETTING_FUNC_TRAILER_MODE = 537268736;
    public static final int SETTING_FUNC_TRANSPARENT_UNDERBODY = 539497728;
    public static final int SETTING_FUNC_TWOSTEP_UNLOCKING = 537922048;

    @VendorDefinition(author = "@ECARX", date = "2021-06-01", project = "smart", requirement = "")
    public static final int SETTING_FUNC_UNLOCK_P_GEAR = 539427072;
    public static final int SETTING_FUNC_VISIBLE_LOCKING_FEEDBACK = 537919744;
    public static final int SETTING_FUNC_VISIBLE_UNLOCKING_FEEDBACK = 537920000;
    public static final int SETTING_FUNC_VOICE_RECOGNITION = 538706688;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "smart", requirement = "")
    public static final int SETTING_FUNC_WAITING_MODE = 539428352;

    @VendorDefinition(author = "@ECARX", date = "2021-09-06", project = "KX11", requirement = "")
    public static final int SETTING_FUNC_WALLPAPER_FESTIVAL_TYPE = 539496192;
    public static final int SETTING_FUNC_WALLPAPER_SYNC = 539033856;
    public static final int SETTING_FUNC_WALLPAPER_SYNC_STYLE = 539034112;
    public static final int SETTING_FUNC_WELCOME_SOUND = 539099392;
    public static final int SETTING_FUNC_WELCOME_SOUND_TYPE = 539099648;
    public static final int SETTING_FUNC_WINDOW_CLOSE_SUNCURTAIN = 537395456;
    public static final int SETTING_FUNC_WINDOW_PINCH_WARN = 537396480;
    public static final int SETTING_FUNC_WINDOW_VENTILATE = 537396736;
    public static final int SETTING_FUNC_WINDSCREEN_SERVICE_POSITION = 537657600;
    public static final int SETTING_FUNC_XCALL_KEY_LOCK = 538313216;
    public static final int SOUND_WARNING_VOLUME_LEVEL_HIGH = 538771715;
    public static final int SOUND_WARNING_VOLUME_LEVEL_LOW = 538771713;
    public static final int SOUND_WARNING_VOLUME_LEVEL_MID = 538771714;
    public static final int SOUND_WARNING_VOLUME_LEVEL_OFF = 0;
    public static final int SPEED_CONTROL_MODE_ACC = 537069058;
    public static final int SPEED_CONTROL_MODE_CC = 537069057;
    public static final int SPEED_CONTROL_MODE_GPILOT = 537069059;
    public static final int SPEED_CONTROL_MODE_OFF = 0;
    public static final int SPEED_GUIDANCE_OFF = 539493633;
    public static final int SPEED_GUIDANCE_SOUND = 539493635;
    public static final int SPEED_GUIDANCE_VISUAL_SOUND = 539493634;
    public static final int SPEED_LIMITATION_MODE_ASL = 537068802;
    public static final int SPEED_LIMITATION_MODE_AVSL = 537068801;
    public static final int SPEED_LIMITATION_MODE_OFF = 0;
    public static final int STEERING_ASSISTANCE_LEVEL_HIGH = 537331713;
    public static final int STEERING_ASSISTANCE_LEVEL_LOW = 537331715;
    public static final int STEERING_ASSISTANCE_LEVEL_MEDIUM = 537331714;
    public static final int STEERING_ASSISTANCE_LEVEL_OFF = 0;
    public static final int SUSPENSION_HEIGHT_ADJUST_LEVEL_HIGH_1 = 538509570;
    public static final int SUSPENSION_HEIGHT_ADJUST_LEVEL_HIGH_2 = 538509569;
    public static final int SUSPENSION_HEIGHT_ADJUST_LEVEL_LOW_1 = 538509572;
    public static final int SUSPENSION_HEIGHT_ADJUST_LEVEL_LOW_2 = 538509573;
    public static final int SUSPENSION_HEIGHT_ADJUST_LEVEL_NORMAL = 538509571;
    public static final int SUSPENSION_HEIGHT_ADJUST_LEVEL_OFF = 0;

    @Deprecated
    public static final int TWOSTEP_UNLOCKING_ALL_DOORS = 537922049;
    public static final int TWOSTEP_UNLOCKING_OFF = 0;
    public static final int TWOSTEP_UNLOCKING_ON = 1;

    @Deprecated
    public static final int TWOSTEP_UNLOCKING_SINGLE_DOOR = 537922050;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AVASSoundType {
        public static final int AVAS_SOUND_TYPE_1 = 538576641;
        public static final int AVAS_SOUND_TYPE_2 = 538576642;
        public static final int AVAS_SOUND_TYPE_3 = 538576643;
        public static final int AVAS_SOUND_TYPE_4 = 538576644;
        public static final int AVAS_SOUND_TYPE_5 = 538576645;
        public static final int AVAS_SOUND_TYPE_6 = 538576646;
        public static final int AVAS_SOUND_TYPE_7 = 538576647;
        public static final int AVAS_SOUND_TYPE_8 = 538576648;
        public static final int AVAS_SOUND_TYPE_NONE = 0;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AVASVolumeLevel {
        public static final int AVAS_VOLUME_LEVEL_HIGH = 538576387;
        public static final int AVAS_VOLUME_LEVEL_LOW = 538576385;
        public static final int AVAS_VOLUME_LEVEL_MID = 538576386;
        public static final int AVAS_VOLUME_LEVEL_OFF = 0;
    }

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    @Retention(RetentionPolicy.SOURCE)
    public @interface AbnormalVehicleAlarm {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AmbienceLightExperienceMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AmbienceLightMainColorMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ArtificialSoundType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoCLoseWindowMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BrightnessDrivingMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BrightnessStationaryMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CarLocatorReminderMode {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    @Retention(RetentionPolicy.SOURCE)
    public @interface CongestionAheadAlarm {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    @Retention(RetentionPolicy.SOURCE)
    public @interface DangerousRoadAlarm {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DayModeSettings {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-09-13", project = "lambda")
    @Retention(RetentionPolicy.SOURCE)
    public @interface DigitalKeyReqStsStatus {
        public static final int CREATING = 1;
        public static final int DELETING = 3;
        public static final int RESETTING = 2;
        public static final int SEARCHING_CARD = 4;
        public static final int SEARCHING_PHONE = 5;
    }

    @VendorDefinition(author = "@ECARX", date = "2021-09-13", project = "lambda")
    @Retention(RetentionPolicy.SOURCE)
    public @interface DigitalKeySuspensionStatus {
        public static final int IDLE = 0;
        public static final int REJECT_BNCM = 5;
        public static final int REJECT_CARMOD = 3;
        public static final int REJECT_KEY_NOT_EXIST = 4;
        public static final int REJECT_USGMOD = 2;
        public static final int SUCCESS = 1;
    }

    @VendorDefinition(author = "@ECARX", date = "2021-09-13", project = "lambda")
    @Retention(RetentionPolicy.SOURCE)
    public @interface DigitalKeyTerminationStatus {
        public static final int IDLE = 0;
        public static final int REJECT_BNCM = 6;
        public static final int REJECT_CARMOD = 3;
        public static final int REJECT_KEY_NOT_EXIST = 4;
        public static final int REJECT_NO_KEY = 5;
        public static final int REJECT_USGMOD = 2;
        public static final int SUCCESS = 1;
    }

    @VendorDefinition(author = "@ECARX", date = "2021-09-13", project = "lambda")
    @Retention(RetentionPolicy.SOURCE)
    public @interface DigitalKeyUnpairStatus {
        public static final int IDLE = 0;
        public static final int REJECT_BNCM = 6;
        public static final int REJECT_CARMOD = 3;
        public static final int REJECT_NO_KEY = 5;
        public static final int REJECT_NO_MOBILE = 4;
        public static final int REJECT_USGMOD = 2;
        public static final int SUCCESS = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DoorOpenWarnVolumeMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ESMVolumeLevel {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    @Retention(RetentionPolicy.SOURCE)
    public @interface EmergencyVehicleAlarm {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EnergyRegenerationLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ExteriorLightControlValue {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-09-06", project = "KX11", requirement = "")
    @Retention(RetentionPolicy.SOURCE)
    public @interface FestivalType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ForwardCollisionWarnSnvtyMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface HmiThemesValue {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface HomeSafeLightValue {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface KeylessUnlockingValue {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LampAutoLightValue {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LampHighbeamActiveValue {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LaneChangeWarningSoundMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LaneKeepingAidMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LaneKeepingAidWarningMode {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-07-02", project = "lambda", requirement = "")
    @Retention(RetentionPolicy.SOURCE)
    public @interface LaunchModeIndcnStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MirrorDimmingLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MirrorDippingMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PEBWorkMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ParkAssistSysVolumeMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ParkComfortModeOffReasonMode {
        public static final int LOW_SOC = 538838273;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ParkingComfortModeTimer {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PrivateLockReminder {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "Lambda")
    @Retention(RetentionPolicy.SOURCE)
    public @interface RearSpoilernAdjustLevel {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    @Retention(RetentionPolicy.SOURCE)
    public @interface RedLightAlarm {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RemoteUnlockingValue {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RotatedWheelsWarningInfo {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScreenSaverStyle {
        public static final int SCREEN_SAVER_STYLE_1 = 539034369;
        public static final int SCREEN_SAVER_STYLE_2 = 539034370;
        public static final int SCREEN_SAVER_STYLE_3 = 539034371;
        public static final int SCREEN_SAVER_STYLE_4 = 539034372;
        public static final int SCREEN_SAVER_STYLE_5 = 539034373;
        public static final int SCREEN_SAVER_STYLE_CUSTOM = 539034623;
        public static final int SCREEN_SAVER_STYLE_NONE = 0;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SoundWarningVolume {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SpeedControlMode {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-07-09", project = "smart", requirement = "")
    @Retention(RetentionPolicy.SOURCE)
    public @interface SpeedGuidanceAlarm {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SpeedLimitationMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SteeringAssistanceLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SuspensionAdjustLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TwoStepUnlockingValue {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface VehicleFunction {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WallpaperStyle {
        public static final int WALLPAPER_STYLE_1 = 539034113;
        public static final int WALLPAPER_STYLE_2 = 539034114;
        public static final int WALLPAPER_STYLE_3 = 539034115;
        public static final int WALLPAPER_STYLE_4 = 539034116;
        public static final int WALLPAPER_STYLE_5 = 539034117;
        public static final int WALLPAPER_STYLE_NONE = 0;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WelcomeSoundType {
        public static final int WELCOME_SOUND_TYPE_1 = 539099649;
        public static final int WELCOME_SOUND_TYPE_2 = 539099650;
        public static final int WELCOME_SOUND_TYPE_3 = 539099651;
        public static final int WELCOME_SOUND_TYPE_NONE = 0;
    }
}
