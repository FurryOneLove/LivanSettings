package com.ecarx.xui.adaptapi.uiinteraction;

import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface ITouchManager {
    public static final int INTERCEPT_MODE_DISPATCH = 1;
    public static final int INTERCEPT_MODE_INTERCEPT = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface InterceptMode {
    }

    boolean registerFullScreenTouchListener(int i, View.OnTouchListener onTouchListener);

    boolean unregisterFullScreenTouchListener(View.OnTouchListener onTouchListener);
}
