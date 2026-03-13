package com.ecarx.xui.adaptapi.ota;

import android.content.Context;
import com.ecarx.xui.adaptapi.AdaptAPI;
import com.ecarx.xui.adaptapi.FunctionStatus;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.Calendar;

/* loaded from: classes.dex */
public abstract class OTA extends AdaptAPI {
    public static final int OTA_TYPE_IHU = 1;
    public static final int OTA_TYPE_IHU_DVR = 34;
    public static final int OTA_TYPE_IHU_VP = 18;
    public static final int OTA_TYPE_TBOX = 33;
    public static final int OTA_TYPE_VP = 17;
    public static final int OTA_TYPE_WHOLE_CAR = 2;

    @Deprecated
    public static final int UPDATE_FAILED_REASON_DEFAULT = 0;

    @Deprecated
    public static final int UPDATE_FAILED_REASON_INSUFFICIENT_STORAGE = 2;

    @Deprecated
    public static final int UPDATE_FAILED_REASON_INVALID_PACKAGE = 1;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface OtaType {
    }

    @Deprecated
    public abstract int getOtaBaseSysVersionCode();

    @Deprecated
    public abstract String getOtaBaseSysVersionName();

    @Deprecated
    public abstract String getOtaPkgRootPath();

    @Deprecated
    public abstract int getSysVersionCode();

    @Deprecated
    public abstract String getSysVersionName();

    public abstract boolean installPackage(IOtaSession iOtaSession, String str);

    public abstract FunctionStatus isOtaTypeSupported(int i);

    public abstract void releaseOtaCallback(IOtaSessionCallback iOtaSessionCallback);

    public abstract IOtaSession requestOta(int i, IOtaSessionCallback iOtaSessionCallback);

    public abstract IOtaSession requestOta(int i, boolean z, IOtaSessionCallback iOtaSessionCallback);

    public abstract IOtaSession requestOta(boolean z, IOtaSessionCallback iOtaSessionCallback);

    @Deprecated
    public abstract boolean setOtaUpdateTime(Calendar calendar);

    public abstract boolean supportNoRecoveryOta();

    public static OTA create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.ota.impl.OTAImpl");
            Constructor c = clazz.getConstructor(Context.class);
            return (OTA) c.newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
