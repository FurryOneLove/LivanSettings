package com.ecarx.xui.adaptapi.car.vehicle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IADAS {
    public static final int AI_ASSIST_LANE_CHANGE_STRATEGY_GENTLE = 671614209;
    public static final int AI_ASSIST_LANE_CHANGE_STRATEGY_OFF = 0;
    public static final int AI_ASSIST_LANE_CHANGE_STRATEGY_RADICAL = 671614211;
    public static final int AI_ASSIST_LANE_CHANGE_STRATEGY_STANDARD = 671614210;
    public static final int AI_ASSIST_LANE_CHANGE_WARNING_BOTH = 671614723;
    public static final int AI_ASSIST_LANE_CHANGE_WARNING_OFF = 0;
    public static final int AI_ASSIST_LANE_CHANGE_WARNING_VIBRATE = 671614722;
    public static final int AI_ASSIST_LANE_CHANGE_WARNING_VOICE = 671614721;
    public static final int AUTOMATIC_PARKING_APA = 671678720;
    public static final int PDC_WARNING_VOLUME_HIGH = 671416579;
    public static final int PDC_WARNING_VOLUME_LOW = 671416577;
    public static final int PDC_WARNING_VOLUME_MID = 671416578;
    public static final int PDC_WARNING_VOLUME_OFF = 0;
    public static final int SETTING_FUNC_ACC_WITH_TSR = 671482624;
    public static final int SETTING_FUNC_AI_ASSIST_DEFAULT_ON = 671613440;
    public static final int SETTING_FUNC_AI_ASSIST_FUSION_NAVI = 671613696;
    public static final int SETTING_FUNC_AI_ASSIST_LANE_CHANGE_CONFIRM = 671614464;
    public static final int SETTING_FUNC_AI_ASSIST_LANE_CHANGE_STRATEGY = 671614208;
    public static final int SETTING_FUNC_AI_ASSIST_LANE_CHANGE_WARNING = 671614720;
    public static final int SETTING_FUNC_AI_ASSIST_OUT_OVERTAKING_LANE = 671613952;
    public static final int SETTING_FUNC_AI_DRIVER_ASSIST = 671613184;
    public static final int SETTING_FUNC_AUTONOMOUS_EMERGENCY_BRAKING = 537333248;
    public static final int SETTING_FUNC_AUTO_LANE_CHANGE_ASSIST = 671351040;
    public static final int SETTING_FUNC_BIG_DATA_SPEED_LIMIT = 671484416;
    public static final int SETTING_FUNC_BLIND_SPOT_DETECTION = 671547648;
    public static final int SETTING_FUNC_BLIND_SPOT_DETECTION_WARNING = 671547904;
    public static final int SETTING_FUNC_DOOR_OPEN_WARN_ACTIVE = 538050816;
    public static final int SETTING_FUNC_DOOR_OPEN_WARN_VOLUME = 538051072;
    public static final int SETTING_FUNC_DOW_WARNING_STATUS = 671416832;
    public static final int SETTING_FUNC_DRIVER_PERFOR_SUPPORT = 537003520;
    public static final int SETTING_FUNC_DRIVER_PERFOR_SUPPORT_REMINDER = 671219968;
    public static final int SETTING_FUNC_DRIVE_PILOT = 671548416;
    public static final int SETTING_FUNC_ELE_SEATBELT_COMFORT = 537333504;
    public static final int SETTING_FUNC_EMGY_LANE_KEEP_AID = 537331200;
    public static final int SETTING_FUNC_EMGY_LANE_OCC_WARNING = 537332480;
    public static final int SETTING_FUNC_EVASIVE_MANEUVER_AID = 537332736;
    public static final int SETTING_FUNC_FORWARD_COLLISION_WARN = 537788672;
    public static final int SETTING_FUNC_FORWARD_COLLISION_WARN_SNVTY = 537788928;
    public static final int SETTING_FUNC_FRONT_CROSS_TRAFFIC_ALERT = 537331968;
    public static final int SETTING_FUNC_HDC_SWITCH = 537265408;
    public static final int SETTING_FUNC_LANE_CHANGE_ASSIST = 537331456;
    public static final int SETTING_FUNC_LANE_CHANGE_ASSIST_WARNING = 671351296;
    public static final int SETTING_FUNC_LANE_CHANGE_WARING = 537330432;
    public static final int SETTING_FUNC_LANE_CHANGE_WARNING_SOUND = 537330688;
    public static final int SETTING_FUNC_LANE_DEPARTURE_WARNING = 671285504;
    public static final int SETTING_FUNC_LANE_KEEPING_AID = 537329920;
    public static final int SETTING_FUNC_LANE_KEEPING_AID_MODE = 537330176;
    public static final int SETTING_FUNC_LANE_KEEPING_AID_WARNING = 537330944;
    public static final int SETTING_FUNC_OTHER_ROAD_SIGH_INFO = 671485952;
    public static final int SETTING_FUNC_PDC_SWITCH = 537264896;
    public static final int SETTING_FUNC_PDC_WARNING_VOLUME = 671416576;
    public static final int SETTING_FUNC_PEB_MODE = 537264640;
    public static final int SETTING_FUNC_PRE_COLLISION_SYS = 671548160;
    public static final int SETTING_FUNC_REAR_COLLISION_WARNING = 537333760;
    public static final int SETTING_FUNC_REAR_CROSS_TRAFFIC_ALERT = 537332224;
    public static final int SETTING_FUNC_SPEED_CONTROL_MODE = 537069056;
    public static final int SETTING_FUNC_SPEED_LIMITATION_MODE = 537068800;
    public static final int SETTING_FUNC_SPEED_LIMIT_WARN = 671482112;
    public static final int SETTING_FUNC_SPEED_LIMIT_WARNING_MODE = 671482368;
    public static final int SETTING_FUNC_SPEED_LIMIT_WARNING_OFFSET = 671482880;
    public static final int SETTING_FUNC_SPEED_LIMIT_WARNING_OFFSET_VALUE = 671483136;
    public static final int SETTING_FUNC_SPEED_LIMIT_WARNING_OFFSET_VALUE_MAX = 671483392;
    public static final int SETTING_FUNC_SPEED_LIMIT_WARNING_OFFSET_VALUE_MIN = 671483648;
    public static final int SETTING_FUNC_SPEED_LIMIT_WARNING_OFFSET_VALUE_STEP = 671483904;
    public static final int SETTING_FUNC_SPEED_LIMIT_WARNING_OFFSET_VALUE_SWITCH = 671484160;
    public static final int SETTING_FUNC_STEERING_ASSISTANCE_LEVEL = 537331712;
    public static final int SETTING_FUNC_TRAFFIC_LIGHT_ATTENTION = 537332992;
    public static final int SETTING_FUNC_TRAFFIC_LIGHT_ATTENTION_SOUND = 671154432;
    public static final int SETTING_FUNC_TRAFFIC_SIGN_RECOGNITION = 537592064;
    public static final int SETTING_FUNC_TRAFFIC_SIGN_RECOGNITION_ALERT = 537592320;
    public static final int SPEED_LIMIT_WARNING_MODE_FLASHING = 671482370;
    public static final int SPEED_LIMIT_WARNING_MODE_NO_WARNING = 671482369;
    public static final int SPEED_LIMIT_WARNING_MODE_OFF = 0;
    public static final int SPEED_LIMIT_WARNING_MODE_SOUND = 671482371;
    public static final int SPEED_LIMIT_WARNING_OFFSET_0KM = 671482881;
    public static final int SPEED_LIMIT_WARNING_OFFSET_10KM = 671482883;
    public static final int SPEED_LIMIT_WARNING_OFFSET_5KM = 671482882;
    public static final int SPEED_LIMIT_WARNING_OFFSET_MINUS_10KM = 671482885;
    public static final int SPEED_LIMIT_WARNING_OFFSET_MINUS_5KM = 671482884;
    public static final int SPEED_LIMIT_WARNING_OFFSET_OFF = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ADASFunction {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DoorOpenWarningStatus {
        public static final int DOW_WARNING_STATUS_LEVEL_1 = 671416833;
        public static final int DOW_WARNING_STATUS_LEVEL_2 = 671416834;
        public static final int DOW_WARNING_STATUS_LEVEL_3 = 671416835;
        public static final int DOW_WARNING_STATUS_NONE = 0;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LaneChangeStrategy {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LaneChangeWarningMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PDCWarningVolumeMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SpeedLimitWarningMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SpeedLimitWarningOffset {
    }
}
