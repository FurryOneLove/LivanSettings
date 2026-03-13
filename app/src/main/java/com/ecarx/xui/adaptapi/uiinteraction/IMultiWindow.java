package com.ecarx.xui.adaptapi.uiinteraction;

import com.ecarx.xui.adaptapi.FunctionStatus;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IMultiWindow {
    public static final int SCREEN_TYPE_BOTTOM = 4;
    public static final int SCREEN_TYPE_LEFT = 1;
    public static final int SCREEN_TYPE_RIGHT = 2;
    public static final int SCREEN_TYPE_TOP = 3;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScreenType {
    }

    void closeSplitScreenMode();

    void closeSplitScreenMode(int i);

    String getScreenStackPackageName(int i);

    boolean isActivitySupportedSplitScreen(String str, String str2);

    boolean isInSplitScreenWindowingMode();

    boolean isPackageSupportedSplitScreen(String str);

    FunctionStatus isSplitScreenModeSupported();

    void swapDockedAndFullscreenStack();
}
