package com.ecarx.xui.adaptapi.car.vehicle;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IPAS {
    public static final int AUTO_REVERSE_CAMERA_OFF = 0;
    public static final int AUTO_REVERSE_CAMERA_REAR = 587400193;
    public static final int AUTO_REVERSE_CAMERA_TOP = 587400194;
    public static final int PAC_3DVIEW_POSITION_FRONT_CENTER = 587403777;
    public static final int PAC_3DVIEW_POSITION_FRONT_LEFT = 587403779;
    public static final int PAC_3DVIEW_POSITION_FRONT_RIGHT = 587403778;
    public static final int PAC_3DVIEW_POSITION_LEFT = 587403780;
    public static final int PAC_3DVIEW_POSITION_OFF = 0;
    public static final int PAC_3DVIEW_POSITION_REAR_CENTER = 587403782;
    public static final int PAC_3DVIEW_POSITION_REAR_LEFT = 587403783;
    public static final int PAC_3DVIEW_POSITION_REAR_RIGHT = 587403784;
    public static final int PAC_3DVIEW_POSITION_RIGHT = 587403781;
    public static final int PAC_CAMERA_360CAM = 587400450;
    public static final int PAC_CAMERA_REARONLY = 587400449;
    public static final int PAC_CAMERA_TYPE_NONE = 0;
    public static final int PAC_SYS_AVA_STATUS_AVAILABLE = 587404033;
    public static final int PAC_SYS_AVA_STATUS_HADNSHAKE_TIMEOUT = 587404036;
    public static final int PAC_SYS_AVA_STATUS_SERVICE_REQUIRED = 587404035;
    public static final int PAC_SYS_AVA_STATUS_UNAVAILABLE = 587404034;
    public static final int PAC_VIEW_SELECTION_2DVIEW = 587403531;
    public static final int PAC_VIEW_SELECTION_3DVIEW = 587403530;
    public static final int PAC_VIEW_SELECTION_DOUBLE_SIDE = 587403529;
    public static final int PAC_VIEW_SELECTION_FRONT = 587403521;
    public static final int PAC_VIEW_SELECTION_FRONT_JUNC = 587403522;
    public static final int PAC_VIEW_SELECTION_FRONT_TOP = 587403523;
    public static final int PAC_VIEW_SELECTION_OFF = 0;
    public static final int PAC_VIEW_SELECTION_REAR = 587403525;
    public static final int PAC_VIEW_SELECTION_REAR_JUNC = 587403526;
    public static final int PAC_VIEW_SELECTION_REAR_LEFT_3D = 587403532;
    public static final int PAC_VIEW_SELECTION_REAR_RIGHT_3D = 587403533;
    public static final int PAC_VIEW_SELECTION_REAR_TOP = 587403528;
    public static final int PAC_VIEW_SELECTION_TOP = 587403524;
    public static final int PAC_VIEW_SELECTION_ZOOMED_REAR = 587403527;
    public static final int PARKING_RADAR_HIGH = 588252163;
    public static final int PARKING_RADAR_LOW = 588252161;
    public static final int PARKING_RADAR_MIDDLE = 588252162;
    public static final int PARKING_RADAR_VOLUME = 588252160;
    public static final int PAS_FUNC_APA_RPA_SWITCH = 587596288;
    public static final int PAS_FUNC_APA_SELF_RECOMMENDED = 587596032;
    public static final int PAS_FUNC_AVP_ACTIVATED = 588251392;
    public static final int PAS_FUNC_ELE_MIRROR_SYS_ACTIVATED = 588251648;
    public static final int PAS_FUNC_PAC_3DVIEW_LOCK = 587404288;
    public static final int PAS_FUNC_PAC_3DVIEW_POSITION = 587403776;
    public static final int PAS_FUNC_PAC_ACTIVATION = 587399424;
    public static final int PAS_FUNC_PAC_APP_INIT_COMPLETED = 587404544;
    public static final int PAS_FUNC_PAC_AUTO_FRONT_ACTIV = 587399936;
    public static final int PAS_FUNC_PAC_AUTO_REVERSE_CAMERA = 587400192;
    public static final int PAS_FUNC_PAC_CAMERA_TYPE = 587400448;
    public static final int PAS_FUNC_PAC_CAR_MODE_TRANSPARENT = 587407616;
    public static final int PAS_FUNC_PAC_NEARBY_OBJ_TRIGGER = 587407872;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "Lambda")
    public static final int PAS_FUNC_PAC_OBSTACLE_DETECTION = 587408128;
    public static final int PAS_FUNC_PAC_OVERLAY_DSTINFO = 587401728;
    public static final int PAS_FUNC_PAC_OVERLAY_STEERPATH = 587401216;
    public static final int PAS_FUNC_PAC_OVERLAY_TOWBAR = 587401472;
    public static final int PAS_FUNC_PAC_STEER_LINK = 587399680;
    public static final int PAS_FUNC_PAC_SYS_AVA_STATUS = 587404032;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "Lambda")
    public static final int PAS_FUNC_PAC_TOP_VIEW_ZOOM_IN = 587408384;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "Lambda")
    public static final int PAS_FUNC_PAC_TOURING_VIEW = 587408640;
    public static final int PAS_FUNC_PAC_VIEW_SELECTION = 587403520;
    public static final int PAS_FUNC_PAS_ACTIVATED = 537723136;
    public static final int PAS_FUNC_PAS_MUTE = 587268608;
    public static final int PAS_FUNC_PAS_RADAR_FRONT_INNER_LEFT = 587333888;
    public static final int PAS_FUNC_PAS_RADAR_FRONT_INNER_RIGHT = 587334144;
    public static final int PAS_FUNC_PAS_RADAR_FRONT_LEFT_SIDE = 587334912;
    public static final int PAS_FUNC_PAS_RADAR_FRONT_OUT_LEFT = 587334400;
    public static final int PAS_FUNC_PAS_RADAR_FRONT_OUT_RIGHT = 587334656;
    public static final int PAS_FUNC_PAS_RADAR_FRONT_RIGHT_SIDE = 587335168;
    public static final int PAS_FUNC_PAS_RADAR_MAX_DISTANCE = 587336960;
    public static final int PAS_FUNC_PAS_RADAR_MIN_DISTANCE = 587337216;
    public static final int PAS_FUNC_PAS_RADAR_REAR_INNER_LEFT = 587336448;
    public static final int PAS_FUNC_PAS_RADAR_REAR_INNER_RIGHT = 587336704;
    public static final int PAS_FUNC_PAS_RADAR_REAR_LEFT_SIDE = 587335424;
    public static final int PAS_FUNC_PAS_RADAR_REAR_OUT_LEFT = 587335936;
    public static final int PAS_FUNC_PAS_RADAR_REAR_OUT_RIGHT = 587336192;
    public static final int PAS_FUNC_PAS_RADAR_REAR_RIGHT_SIDE = 587335680;
    public static final int PAS_FUNC_PAS_RADAR_WORK_MODE = 587337728;
    public static final int PAS_FUNC_PAS_RADAR_WORK_STATUS = 587337984;
    public static final int PAS_FUNC_PAS_SHOW_GRAPHICS = 587269376;
    public static final int PAS_FUNC_PAS_STATUS = 587268352;
    public static final int PAS_FUNC_PAS_TOP_VIEW = 587269120;
    public static final int PAS_FUNC_PAS_TRAILER_PRESENT = 587268864;
    public static final int PAS_FUNC_PAS_VOLUME = 537723392;
    public static final int PAS_FUNC_RCTA_ACTIVATION = 587530496;
    public static final int PAS_FUNC_RCTA_LEFT_WARNING = 587530752;
    public static final int PAS_FUNC_RCTA_RIGHT_WARNING = 587531008;
    public static final int PAS_FUNC_RCTA_SHOW_GRAPHICS = 587531264;
    public static final int PAS_FUNC_RCTA_WARNING_VOLUME = 587531520;
    public static final int PAS_FUNC_SAP_ACTIVATION = 587464960;
    public static final int PAS_FUNC_SAP_PARK_IN_NOTI = 587469056;
    public static final int PAS_FUNC_SAP_PARK_IN_RESUME = 587465728;
    public static final int PAS_FUNC_SAP_PARK_IN_TYPE = 587465472;
    public static final int PAS_FUNC_SAP_PARK_IN_TYPE_RECOMMEND = 587466496;
    public static final int PAS_FUNC_SAP_PARK_OUT_COMFIRM = 587465984;
    public static final int PAS_FUNC_SAP_PARK_OUT_NOTI = 587469312;
    public static final int PAS_FUNC_SAP_PARK_TYPE = 587465216;
    public static final int PAS_FUNC_SAP_PROGRESS = 587466240;
    public static final int PAS_RADAR_WORK_MODE_FAILURE = 587337733;
    public static final int PAS_RADAR_WORK_MODE_FRONT_ACTIVE = 587337731;
    public static final int PAS_RADAR_WORK_MODE_FRONT_REAR_ACTIVE = 587337730;
    public static final int PAS_RADAR_WORK_MODE_INHIBITED = 587337734;
    public static final int PAS_RADAR_WORK_MODE_OFF = 0;
    public static final int PAS_RADAR_WORK_MODE_REAR_ACTIVE = 587337732;
    public static final int PAS_RADAR_WORK_MODE_STANDBY = 587337729;
    public static final int PAS_RADAR_WORK_STATUS_FRONT_ACTIVE_REAR_FAILURE = 587337985;
    public static final int PAS_RADAR_WORK_STATUS_FRONT_INACTIVE_REAR_FAILURE = 587337988;
    public static final int PAS_RADAR_WORK_STATUS_NONE = 0;
    public static final int PAS_RADAR_WORK_STATUS_REAR_ACTIVE_FRONT_FAILURE = 587337986;
    public static final int PAS_RADAR_WORK_STATUS_REAR_INACTIVE_FRONT_FAILURE = 587337987;
    public static final int PDC_VOLUME_HIGH = 588251907;
    public static final int PDC_VOLUME_LOW = 588251905;
    public static final int PDC_VOLUME_MIDDLE = 588251906;
    public static final int PDC_VOLUME_REQUEST = 588251904;
    public static final int RCTA_WARNING_VOLUME_HIGH = 587531523;
    public static final int RCTA_WARNING_VOLUME_LOW = 587531521;
    public static final int RCTA_WARNING_VOLUME_MID = 587531522;
    public static final int RCTA_WARNING_VOLUME_OFF = 0;
    public static final int SAP_PARK_IN_NOTI_CANCEL_DRIVER_DEACTIVATION = 587469080;
    public static final int SAP_PARK_IN_NOTI_CANCEL_HIGH_SPEED = 587469076;
    public static final int SAP_PARK_IN_NOTI_CANCEL_SLIPPERY_ROAD = 587469077;
    public static final int SAP_PARK_IN_NOTI_CANCEL_STEERING_INTERVENTION = 587469079;
    public static final int SAP_PARK_IN_NOTI_CANCEL_TRAILER_CONNECT = 587469078;
    public static final int SAP_PARK_IN_NOTI_CANCEL_WRONG_GEAR = 587469081;
    public static final int SAP_PARK_IN_NOTI_DRV_FWD_SLOWLY = 587469069;
    public static final int SAP_PARK_IN_NOTI_EMGY_BRK_ACTV = 587469083;
    public static final int SAP_PARK_IN_NOTI_EMGY_BRK_DIS = 587469084;
    public static final int SAP_PARK_IN_NOTI_FAIL_TOO_MANY_MOVES = 587469074;
    public static final int SAP_PARK_IN_NOTI_FINISHED = 587469072;
    public static final int SAP_PARK_IN_NOTI_FIRST_REV_SLOWLY = 587469067;
    public static final int SAP_PARK_IN_NOTI_LOOK_AROUND_LEFT = 587469065;
    public static final int SAP_PARK_IN_NOTI_LOOK_AROUND_RIGHT = 587469066;
    public static final int SAP_PARK_IN_NOTI_NO_NOTIFICATION = 587469057;
    public static final int SAP_PARK_IN_NOTI_OVER_SPEED_SCAN = 587469061;
    public static final int SAP_PARK_IN_NOTI_PARK_FOUND_STOP = 587469062;
    public static final int SAP_PARK_IN_NOTI_PUT_REV_GEAR = 587469063;
    public static final int SAP_PARK_IN_NOTI_PUT_REV_GEAR_AGAIN = 587469064;
    public static final int SAP_PARK_IN_NOTI_RESUMED = 587469082;
    public static final int SAP_PARK_IN_NOTI_REV_SLOWLY = 587469071;
    public static final int SAP_PARK_IN_NOTI_SCAN_LEFT = 587469059;
    public static final int SAP_PARK_IN_NOTI_SCAN_RIGHT = 587469058;
    public static final int SAP_PARK_IN_NOTI_SLOT_FOUND_CON_FWD = 587469060;
    public static final int SAP_PARK_IN_NOTI_STOP_PUT_FWD_GEAR = 587469068;
    public static final int SAP_PARK_IN_NOTI_STOP_PUT_REV_GEAR = 587469070;
    public static final int SAP_PARK_IN_NOTI_TEMP_NOT_AVAIL = 587469075;
    public static final int SAP_PARK_IN_TYPE_PARA = 587465474;
    public static final int SAP_PARK_IN_TYPE_PERP = 587465473;
    public static final int SAP_PARK_OUT_CANCEL_DRIVER_DEACTIVATION = 587469334;
    public static final int SAP_PARK_OUT_CANCEL_HIGH_SPEED = 587469330;
    public static final int SAP_PARK_OUT_CANCEL_SLIPPERY_ROAD = 587469331;
    public static final int SAP_PARK_OUT_CANCEL_STEERING_INTERVENTION = 587469333;
    public static final int SAP_PARK_OUT_CANCEL_TRAILER_CONNECT = 587469332;
    public static final int SAP_PARK_OUT_CHANGE_GEAR = 587469318;
    public static final int SAP_PARK_OUT_CHANGE_GEAR_REV = 587469319;
    public static final int SAP_PARK_OUT_DRV_FWD_SLOWLY = 587469324;
    public static final int SAP_PARK_OUT_FAIL_NO_PATH = 587469328;
    public static final int SAP_PARK_OUT_FAIL_TOO_MANY_MOVES = 587469327;
    public static final int SAP_PARK_OUT_FINISHED = 587469326;
    public static final int SAP_PARK_OUT_LOOK_AROUND_LEFT = 587469320;
    public static final int SAP_PARK_OUT_LOOK_AROUND_RIGHT = 587469321;
    public static final int SAP_PARK_OUT_NOTI_EMGY_BRK_ACTV = 587469337;
    public static final int SAP_PARK_OUT_NOTI_EMGY_BRK_DIS = 587469338;
    public static final int SAP_PARK_OUT_NO_NOTIFICATION = 587469313;
    public static final int SAP_PARK_OUT_RESUMED = 587469335;
    public static final int SAP_PARK_OUT_REV_SLOWLY = 587469322;
    public static final int SAP_PARK_OUT_SCAN_LEFT = 587469316;
    public static final int SAP_PARK_OUT_SCAN_RIGHT = 587469317;
    public static final int SAP_PARK_OUT_SELECT_PARK_OUT = 587469315;
    public static final int SAP_PARK_OUT_SELECT_PARK_OUT_CONFIRM = 587469314;
    public static final int SAP_PARK_OUT_SLOT_TOO_SMALL = 587469336;
    public static final int SAP_PARK_OUT_STOP_PUT_FWD_GEAR = 587469323;
    public static final int SAP_PARK_OUT_STOP_PUT_REV_GEAR = 587469325;
    public static final int SAP_PARK_OUT_TEMP_NOT_AVAIL = 587469329;
    public static final int SAP_PARK_TYPE_IN = 587465217;
    public static final int SAP_PARK_TYPE_OUT = 587465218;
    public static final int SAP_REC_PARK_IN_TYPE_PARA = 587466498;
    public static final int SAP_REC_PARK_IN_TYPE_PERP = 587466497;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Pac3DViewPosition {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PacCameraType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PacSysAvailableStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PacViewSelecion {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ParkingRadarType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PasFunction {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PdcVolumeType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RCTAWarningVolumeMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RadarWorkMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RadarWorkStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ReverseCamera {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SapParkInNotification {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SapParkInType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SapParkInTypeRecommend {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SapParkOutNotification {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SapParkType {
    }
}
