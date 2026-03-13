package com.ecarx.xui.adaptapi.navigation.dr;

import com.ecarx.xui.adaptapi.FunctionStatus;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Deprecated
/* loaded from: classes.dex */
public interface IDrAutoByMap {
    public static final int DR_RATE_FAST = 1;
    public static final int DR_RATE_FASTEST = 0;
    public static final int DR_RATE_NORMAL = 3;
    public static final int DR_RATE_SLOW = 4;
    public static final int DR_RATE_SLOWEST = 5;
    public static final int DR_RATE_UI = 2;
    public static final int KEY_STRING_ACCE = 2;
    public static final int KEY_STRING_GYRO = 1;
    public static final int KEY_STRING_MOUNTANGLE = 4;
    public static final int KEY_STRING_PULES = 3;
    public static final int KEY_STRING_W4M = 5;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DrRate {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface KeyType {
    }

    public interface OnDrChangedListener {
        void onSensorChanged(int i, IDrAPInfo iDrAPInfo);

        void onSensorChanged(IDrAPInfo iDrAPInfo);
    }

    IDrAPInfo getDrLatestInfo(int i);

    int[] getSupportedKeyTypes();

    FunctionStatus isKeyTypeSupported(int i);

    boolean registerListener(OnDrChangedListener onDrChangedListener, int i);

    boolean registerListener(OnDrChangedListener onDrChangedListener, int i, int i2);

    boolean registerListener(OnDrChangedListener onDrChangedListener, int[] iArr, int i);

    void unregisterListener(OnDrChangedListener onDrChangedListener);
}
