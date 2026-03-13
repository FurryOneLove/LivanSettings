package com.ecarx.xui.adaptapi.tbox;

import com.ecarx.xui.adaptapi.FunctionStatus;

@Deprecated
/* loaded from: classes.dex */
public interface ITBoxSettings {

    public interface ICarLocatorCallback {
        void onCarLocatorEnabled(boolean z);
    }

    public interface IKeylockCallback {
        void onKeylockEnabled(boolean z);
    }

    boolean isCarLocatorEnabled();

    FunctionStatus isCarLocatorSupported();

    boolean isKeylockEnabled();

    FunctionStatus isKeylockSupported();

    void setCarLocatorCallback(ICarLocatorCallback iCarLocatorCallback);

    void setCarLocatorEnable(boolean z);

    void setKeylockCallback(IKeylockCallback iKeylockCallback);

    void setKeylockEnable(boolean z);

    void unsetCarLocatorCallback(ICarLocatorCallback iCarLocatorCallback);

    void unsetKeylockCallback(IKeylockCallback iKeylockCallback);
}
