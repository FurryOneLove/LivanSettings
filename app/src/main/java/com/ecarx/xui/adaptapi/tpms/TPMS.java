package com.ecarx.xui.adaptapi.tpms;

import android.content.Context;
import android.util.Log;
import com.ecarx.xui.adaptapi.AdaptAPI;
import com.ecarx.xui.adaptapi.FunctionStatus;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public abstract class TPMS extends AdaptAPI {

    @Deprecated
    public static final int MODE_DTPMS = 1;
    public static final int MODE_ITPMS = 0;

    @Deprecated
    public static final int TIRE_ID_ALL = 0;

    @Deprecated
    public static final int TIRE_ID_LEFT_FRONT = 1;

    @Deprecated
    public static final int TIRE_ID_LEFT_REAR = 2;

    @Deprecated
    public static final int TIRE_ID_RIGHT_FRONT = 3;

    @Deprecated
    public static final int TIRE_ID_RIGHT_REAR = 4;

    @Deprecated
    public static final String XUI_INTENT_ACTION_TPMS_WARNING = "xui.intent.action.TPMS_WARNING";

    @Deprecated
    public static final String XUI_INTENT_EXTRA_TPMS_WARNING_TIRE = "xui.intent.extra.TPMS_WARNING_TIRE";

    @Deprecated
    public interface ITireStateMonitor {
        void onTireStateChanged(int i, ITireState iTireState);
    }

    @Retention(RetentionPolicy.SOURCE)
    @Deprecated
    @interface TireId {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface TpmsMode {
    }

    public abstract ICalibrator getTireCalibrator();

    @Deprecated
    public abstract ITireState getTireState(int i);

    public abstract FunctionStatus isTirePressureCalibrationSupported(int i);

    @Deprecated
    public abstract boolean registerTireStateMonitor(ITireStateMonitor iTireStateMonitor);

    @Deprecated
    public abstract boolean unregisterTireStateMonitor(ITireStateMonitor iTireStateMonitor);

    public static TPMS create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.tpms.impl.TPMSImpl");
            Constructor c = clazz.getConstructor(Context.class);
            return (TPMS) c.newInstance(context);
        } catch (Exception e) {
            Log.e("TPMS", "new TPMS ex:", e);
            return null;
        }
    }
}
