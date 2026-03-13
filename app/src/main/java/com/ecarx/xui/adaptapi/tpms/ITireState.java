package com.ecarx.xui.adaptapi.tpms;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Deprecated
/* loaded from: classes.dex */
public interface ITireState {
    public static final int WARNING_LEVEL_HARD_WARN = 2;
    public static final int WARNING_LEVEL_NO_WARN = 0;
    public static final int WARNING_LEVEL_SENSOR_FAULT = 3;
    public static final int WARNING_LEVEL_SOFT_WARN = 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface TireWarning {
    }

    float getPressure();

    float getTemperature();

    int getTireWarning();

    boolean hasPressureWarning();

    boolean isQuickLeaking();
}
