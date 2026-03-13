package com.ecarx.xui.adaptapi.car.diagnostics;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IECU {
    public static final int ECU_TYPE_AUD = 5;
    public static final int ECU_TYPE_CCSM = 4;
    public static final int ECU_TYPE_CSD = 2;
    public static final int ECU_TYPE_IHU = 1;
    public static final int ECU_TYPE_PAC = 8;
    public static final int ECU_TYPE_TEM2 = 6;
    public static final int ECU_TYPE_UNKNOWN = 0;
    public static final int ECU_TYPE_VCM = 7;
    public static final int ECU_TYPE_WPC = 3;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }
}
