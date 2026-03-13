package com.ecarx.xui.adaptapi.wifiap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IWifiAPHost {
    public static final int WIFIAP_FREQUENCY_2 = 1;
    public static final int WIFIAP_FREQUENCY_5 = 2;

    public interface IWifiAPFrequencyChangeCallBack {
        void onWifiAPFrequencyChange(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiAPFrequency {
    }

    int getCurrentFrequencyMode();

    int[] getSupportedWifiAPFrequency();

    boolean registerWifiAPFrequencyCallBack(IWifiAPFrequencyChangeCallBack iWifiAPFrequencyChangeCallBack);

    void setFrequencyMode(int i);

    boolean unregisterWifiAPFrequencyCallBack(IWifiAPFrequencyChangeCallBack iWifiAPFrequencyChangeCallBack);
}
