package com.ecarx.xui.adaptapi.car.vehicle;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface ISafety {

    @VendorDefinition(author = "@ECARX", date = "2021-04-09", project = "Smart")
    public static final int APPROACH_UNLOCK_DOORS = 738263313;

    @VendorDefinition(author = "@ECARX", date = "2021-04-09", project = "Smart")
    public static final int APPROACH_UNLOCK_SINGLE_DOOR = 738263314;
    public static final int ENGINE_OFF_UNLOCKING_ALL_DOORS = 739246849;
    public static final int ENGINE_OFF_UNLOCKING_OFF = 0;
    public static final int ENGINE_OFF_UNLOCKING_SINGLE_DOOR = 739246850;
    public static final int KEY_LOST_WARNING_OFF = 738330625;
    public static final int KEY_LOST_WARNING_VISUAL = 738330626;
    public static final int KEY_LOST_WARNING_VISUAL_AUDIO = 738330627;
    public static final int SETTING_FUNC_ANY_DOOR_LOCK_WARNING = 738329600;
    public static final int SETTING_FUNC_APPROACH_TAIL_UNLOCK = 738264320;
    public static final int SETTING_FUNC_APPROACH_UNLOCK = 738263296;

    @VendorDefinition(author = "@ECARX", date = "2021-04-09", project = "Smart")
    public static final int SETTING_FUNC_APPROACH_UNLOCK_DOOR_AREA = 738263312;
    public static final int SETTING_FUNC_AUDIBLE_LOCKING_FEEDBACK = 537920256;
    public static final int SETTING_FUNC_AUTO_POWER_DOOR = 738265344;
    public static final int SETTING_FUNC_AWAY_LOCK = 738263552;
    public static final int SETTING_FUNC_CENTRAL_LOCK = 537921792;

    @VendorDefinition(author = "@ECARX", date = "2021-03-30", project = "Smart", requirement = "")
    public static final int SETTING_FUNC_DOUBLE_LOCK = 738329856;
    public static final int SETTING_FUNC_ENGINE_OFF_UNLOCKING = 739246848;
    public static final int SETTING_FUNC_KEYLESS_TRUNK_UNLOCK = 738264576;
    public static final int SETTING_FUNC_KEYLESS_UNLOCKING = 537920512;
    public static final int SETTING_FUNC_KEY_INCAR_REMINDER = 738329088;

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "Lambda")
    public static final int SETTING_FUNC_KEY_LOST_WARNING = 738330624;
    public static final int SETTING_FUNC_PASSIVE_ARMING = 537921280;
    public static final int SETTING_FUNC_P_GEAR_UNLOCK = 738265600;
    public static final int SETTING_FUNC_REDUCED_GUARD = 537921536;
    public static final int SETTING_FUNC_REMOTE_UNLOCKING = 537920768;
    public static final int SETTING_FUNC_SPEED_AUTO_LOCKING_MODE = 739247104;
    public static final int SETTING_FUNC_SPEED_LOCKING = 537921024;
    public static final int SETTING_FUNC_TERMINAL_NOT_OFF_WARN = 738328832;
    public static final int SETTING_FUNC_TIRE_PRESSURE_MONITORING = 537004288;
    public static final int SETTING_FUNC_TRUNK_OPENING_POSITION = 738265088;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    public static final int SETTING_FUNC_TRUNK_STATE = 738330112;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11/EX11")
    public static final int SETTING_FUNC_TRUNK_TAILGATE_TIME_OUT = 738330368;
    public static final int SETTING_FUNC_TRUNK_UNLOCK_DISTANCE = 738264832;
    public static final int SETTING_FUNC_TWOSTEP_UNLOCKING = 537922048;
    public static final int SETTING_FUNC_VISIBLE_LOCKING_FEEDBACK = 537919744;
    public static final int SETTING_FUNC_VISIBLE_UNLOCKING_FEEDBACK = 537920000;
    public static final int SETTING_FUNC_WARNING_TYPE = 738329344;
    public static final int SPEED_AUTO_LOCKING_MODE_10KM = 739247105;
    public static final int SPEED_AUTO_LOCKING_MODE_20KM = 739247106;
    public static final int SPEED_AUTO_LOCKING_MODE_OFF = 0;
    public static final int TIRE_PRESSURE_MONITORING_MODE_COMPLETED = 537004291;
    public static final int TIRE_PRESSURE_MONITORING_MODE_FAILED = 537004292;
    public static final int TIRE_PRESSURE_MONITORING_MODE_INITIATED = 537004289;
    public static final int TIRE_PRESSURE_MONITORING_MODE_NOCALREQ = 0;
    public static final int TIRE_PRESSURE_MONITORING_MODE_ONGOING = 537004290;
    public static final int TRUNK_OPENING_POSITION_LEVEL_1 = 738265089;
    public static final int TRUNK_OPENING_POSITION_LEVEL_2 = 738265090;
    public static final int TRUNK_OPENING_POSITION_LEVEL_3 = 738265091;
    public static final int TRUNK_OPENING_POSITION_LEVEL_4 = 738265092;
    public static final int TRUNK_OPENING_POSITION_LEVEL_5 = 738265093;
    public static final int TRUNK_OPENING_POSITION_OFF = 0;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    public static final int TRUNK_STATE_FULL_CLOSE = 738330114;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    public static final int TRUNK_STATE_FULL_OPEN = 738330118;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    public static final int TRUNK_STATE_HALF_CLOSE = 738330128;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    public static final int TRUNK_STATE_MOVE_DOWN = 738330119;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    public static final int TRUNK_STATE_MOVE_DOWN_BREAK = 738330120;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    public static final int TRUNK_STATE_MOVE_UP = 738330115;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    public static final int TRUNK_STATE_MOVE_UP_BREAK = 738330116;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    public static final int TRUNK_STATE_STOP_DURING_CLOSE = 738330121;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    public static final int TRUNK_STATE_STOP_DURING_OPEN = 738330117;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    public static final int TRUNK_STATE_STOP_MIN_POSITION = 738330129;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    public static final int TRUNK_STATE_UNKNOW = 738330113;
    public static final int TRUNK_UNLOCK_DISTANCE_LEVEL_1 = 738264833;
    public static final int TRUNK_UNLOCK_DISTANCE_LEVEL_2 = 738264834;
    public static final int TRUNK_UNLOCK_DISTANCE_OFF = 0;
    public static final int WARNING_TYPE_LIGHT = 738329345;
    public static final int WARNING_TYPE_LIGHT_VOICE = 738329346;
    public static final int WARNING_TYPE_OFF = 0;

    @VendorDefinition(author = "@ECARX", date = "2021-04-09", project = "Smart")
    @Retention(RetentionPolicy.SOURCE)
    public @interface ApproachUnlockDoor {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EngineOffUnlockingValue {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-6-18", project = "Lambda")
    @Retention(RetentionPolicy.SOURCE)
    public @interface KeyLostWarningState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SafetyFunction {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SpeedAutoLockingMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TirePressureMonitoringState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TrunkOpenPositionLevel {
    }

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11")
    @Retention(RetentionPolicy.SOURCE)
    public @interface TrunkState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TrunkUnlockDistanceLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WarningType {
    }
}
