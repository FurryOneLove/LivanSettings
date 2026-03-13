package com.ecarx.xui.adaptapi.wifiap;

import android.content.Context;
import com.ecarx.xui.adaptapi.FunctionStatus;
import java.lang.reflect.Constructor;
import java.util.List;

/* loaded from: classes.dex */
public abstract class Wifi6Ap extends WifiAp {
    @Override // com.ecarx.xui.adaptapi.wifiap.WifiAp
    public abstract int getMaxConnections();

    public abstract boolean getWifi6ApEnabled();

    public abstract int getWifi6ApFrequencyBand();

    public abstract String getWifi6ApPassword();

    public abstract String getWifi6ApSSID();

    @Override // com.ecarx.xui.adaptapi.wifiap.WifiAp
    public abstract IWifiAPHost getWifiAPHost();

    @Override // com.ecarx.xui.adaptapi.wifiap.WifiAp
    public abstract List<IWifiApClient> getWifiApClients();

    @Override // com.ecarx.xui.adaptapi.wifiap.WifiAp
    public abstract FunctionStatus isWifiAPSupported();

    @Override // com.ecarx.xui.adaptapi.wifiap.WifiAp
    public abstract FunctionStatus isWifiSupported();

    @Override // com.ecarx.xui.adaptapi.wifiap.WifiAp
    public abstract boolean setMaxConnections(int i);

    public abstract boolean setWifi6ApConfiguration(String str, String str2, int i, int i2);

    public abstract boolean setWifi6ApEnabled(boolean z);

    @Override // com.ecarx.xui.adaptapi.wifiap.WifiAp
    public abstract boolean setWifiApClientCallback(IWifiApClientCallback iWifiApClientCallback);

    @Override // com.ecarx.xui.adaptapi.wifiap.WifiAp
    public abstract boolean unsetWifiApClientCallback(IWifiApClientCallback iWifiApClientCallback);

    public static Wifi6Ap create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.wifiap.impl.Wifi6ApImpl");
            Constructor c = clazz.getConstructor(Context.class);
            return (Wifi6Ap) c.newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
