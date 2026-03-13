package com.ecarx.xui.adaptapi.navigation.ehp.v2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IHznSplyElectcStatus extends IV2Message {
    public static final int EHP_STATUS_NOT_RUNNING = 2;
    public static final int EHP_STATUS_NOT_SUPPORT = 1;
    public static final int EHP_STATUS_RUNNING = 3;
    public static final int EHP_STATUS_UNKNOWN = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface EHPStatus {
    }

    int getStatus();
}
