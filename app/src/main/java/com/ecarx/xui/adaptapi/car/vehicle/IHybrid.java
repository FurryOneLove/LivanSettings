package com.ecarx.xui.adaptapi.car.vehicle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IHybrid {
    public static final int HYBRID_FUNC_BATTERY_CHARGE_MODE = 604111360;
    public static final int HYBRID_FUNC_BATTERY_SAVE_MODE = 604111104;
    public static final int HYBRID_FUNC_BATTERY_SOC = 604176640;
    public static final int HYBRID_FUNC_POWER_FLOW = 604045568;
    public static final int POWER_FLOW_BOOST = 604045570;
    public static final int POWER_FLOW_CHARGE_AC = 604045582;
    public static final int POWER_FLOW_CHARGE_DC = 604045583;
    public static final int POWER_FLOW_DISCHARGE = 604045584;
    public static final int POWER_FLOW_EAWD = 604045571;
    public static final int POWER_FLOW_ELEC = 604045574;
    public static final int POWER_FLOW_ENGINEOFF_REGBRAKE = 604045578;
    public static final int POWER_FLOW_ENGINEONLY = 604045572;
    public static final int POWER_FLOW_ENGINEONLY_CHARGE = 604045573;
    public static final int POWER_FLOW_ENGINEON_REGBRAKE = 604045579;
    public static final int POWER_FLOW_ENGINEON_REGBRAKE_CHARGE = 604045580;
    public static final int POWER_FLOW_FRONT_ELE_DRIVE = 604045586;
    public static final int POWER_FLOW_MAIN_CHARGE = 604045569;
    public static final int POWER_FLOW_NOT_READY = 0;
    public static final int POWER_FLOW_PURE_ELE_AWD = 604045585;
    public static final int POWER_FLOW_REAR_ELE_DRIVE = 604045587;
    public static final int POWER_FLOW_REGENERATION = 604045589;
    public static final int POWER_FLOW_SAILING = 604045581;
    public static final int POWER_FLOW_STANDSTILL = 604045588;
    public static final int POWER_FLOW_STILL_ENGINEOFF = 604045575;
    public static final int POWER_FLOW_STILL_ENGINEON = 604045576;
    public static final int POWER_FLOW_STILL_ENGINEON_CHARGE = 604045577;

    @Retention(RetentionPolicy.SOURCE)
    public @interface HybridFunction {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PowerFlowMode {
    }
}
