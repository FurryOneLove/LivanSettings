package com.ecarx.xui.adaptapi.dvr.forp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IDvrInfo {
    public static final int DVR_MODEL_NAME = 1;
    public static final int DVR_SOFTWARE_VERSION = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface StrInfoId {
    }

    String getDvrInfoString(int i);
}
