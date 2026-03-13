package com.ecarx.xui.adaptapi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public final class Operator {
    public static final int DEFAULT = 0;
    public static final int GESTURE = 4;
    public static final int QUICK_SETTING = 8;
    public static final int SETTING = 16;
    public static final int VR = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }
}
