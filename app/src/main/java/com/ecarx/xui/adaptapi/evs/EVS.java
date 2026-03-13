package com.ecarx.xui.adaptapi.evs;

import android.content.Context;
import com.ecarx.xui.adaptapi.AdaptAPI;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public abstract class EVS extends AdaptAPI {
    public static final int EVS_CAMERA_AVM = 2;
    public static final int EVS_CAMERA_DVR = 3;
    public static final int EVS_CAMERA_REAR = 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface EvsCameraId {
    }

    public interface IEvsCameraStatusObserver {
        void onEvsCameraClosed(int i);

        void onEvsCameraOpened(int i);
    }

    public abstract boolean attachEvsCameraStatusObserver(IEvsCameraStatusObserver iEvsCameraStatusObserver);

    public abstract boolean detachEvsCameraStatusObserver(IEvsCameraStatusObserver iEvsCameraStatusObserver);

    public abstract boolean evsCameraCloseNotify(int i);

    @Deprecated
    public abstract IEvsCamera getEvsCamera();

    public abstract boolean isCameraOpened(int i);

    public static EVS create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.evs.impl.EvsImpl");
            Constructor c = clazz.getConstructor(new Class[0]);
            return (EVS) c.newInstance(new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
