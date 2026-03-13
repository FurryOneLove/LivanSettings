package com.ecarx.xui.adaptapi.dvr.forp;

import com.ecarx.xui.adaptapi.FunctionStatus;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IDvrFunction {
    public static final int COMMON_VALUE_ERROR = 253;
    public static final int COMMON_VALUE_OFF = 0;
    public static final int COMMON_VALUE_ON = 1;
    public static final int COMMON_VALUE_UNKNOWN = 255;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CommonValue {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DvrFunction {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DvrFunctionValue {
    }

    public interface IFunctionValueWatcher {
        void onCustomizeFunctionValueChanged(int i, float f);

        void onFunctionValueChanged(int i, int i2);
    }

    float getCustomizeFunctionValue(int i);

    int getFunctionValue(int i);

    int[] getSupportedFunctionValue(int i);

    FunctionStatus isFunctionSupported(int i);

    boolean registerFunctionValueWatcher(IFunctionValueWatcher iFunctionValueWatcher);

    boolean setCustomizeFunctionValue(int i, float f);

    boolean setFunctionValue(int i, int i2);

    boolean unregisterFunctionValueWatcher(IFunctionValueWatcher iFunctionValueWatcher);
}
