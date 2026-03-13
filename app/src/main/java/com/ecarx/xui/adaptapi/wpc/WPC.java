package com.ecarx.xui.adaptapi.wpc;

import android.content.Context;
import android.util.Log;
import com.ecarx.xui.adaptapi.AdaptAPI;
import com.ecarx.xui.adaptapi.FunctionStatus;
import java.lang.reflect.Constructor;

@Deprecated
/* loaded from: classes.dex */
public abstract class WPC extends AdaptAPI {
    public static final int CHARGING_STATUS_CHARGING = 2;
    public static final int CHARGING_STATUS_ERROR = 5;
    public static final int CHARGING_STATUS_FOD = 9;
    public static final int CHARGING_STATUS_FULLY_CHARGED = 3;
    public static final int CHARGING_STATUS_INTERRUPT_PEPS = 10;
    public static final int CHARGING_STATUS_NO_DEVICE = 1;
    public static final int CHARGING_STATUS_OFF = -2147483647;
    public static final int CHARGING_STATUS_OVERHEAT_OR_FOD = 4;
    public static final int CHARGING_STATUS_OVERHEAT_PROTECTED = 8;
    public static final int CHARGING_STATUS_STANDBY = 7;
    public static final int CHARGING_STATUS_TAKE_MOBILE_DEVICE = 6;
    public static final int WORKING_MODE_AUTO = 2;
    public static final int WORKING_MODE_NONE = 0;
    public static final int WORKING_MODE_OFF = 1;

    @interface ChargingStatus {
    }

    public interface StateListener {
        void onChargingStatus(@ChargingStatus int i);

        void onWorkingMode(@WorkingMode int i);
    }

    @interface WorkingMode {
    }

    @ChargingStatus
    public abstract int getChargingStatus();

    @WorkingMode
    public abstract int getWorkingMode();

    public abstract FunctionStatus isWPCSupported();

    public abstract void setStateListener(StateListener stateListener);

    public abstract int setWorkingMode(@WorkingMode int i);

    public abstract void unsetStateListener(StateListener stateListener);

    public static WPC create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.wpc.impl.WPCImpl");
            Constructor c = clazz.getConstructor(Context.class);
            return (WPC) c.newInstance(context);
        } catch (Exception e) {
            Log.e("WPC", "WPC Device ex:", e);
            return null;
        }
    }
}
