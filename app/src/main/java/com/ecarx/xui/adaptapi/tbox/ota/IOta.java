package com.ecarx.xui.adaptapi.tbox.ota;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IOta {
    public static final int FAILED_REASON_DEVICE_BUSY = 2;
    public static final int FAILED_REASON_DEVICE_DISCONNECTED = 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface OtaFailedReason {
    }

    int getOtaBaseSoftwareVersionCode();

    String getOtaBaseSoftwareVersionName();

    boolean installPackage(IOtaSession iOtaSession, String str);

    void releaseOtaCallback(IOtaSessionCallback iOtaSessionCallback);

    IOtaSession requestOta(IOtaSessionCallback iOtaSessionCallback);
}
