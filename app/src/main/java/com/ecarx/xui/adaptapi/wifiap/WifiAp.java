package com.ecarx.xui.adaptapi.wifiap;

import android.content.Context;
import com.ecarx.xui.adaptapi.AdaptAPI;
import com.ecarx.xui.adaptapi.FunctionStatus;
import java.lang.reflect.Constructor;
import java.util.List;

/* loaded from: classes.dex */
public abstract class WifiAp extends AdaptAPI {
    public abstract int getMaxConnections();

    public abstract IWifiAPHost getWifiAPHost();

    public abstract List<IWifiApClient> getWifiApClients();

    public abstract FunctionStatus isWifiAPSupported();

    public abstract FunctionStatus isWifiSupported();

    public abstract boolean setMaxConnections(int i);

    public abstract boolean setWifiApClientCallback(IWifiApClientCallback iWifiApClientCallback);

    public abstract boolean unsetWifiApClientCallback(IWifiApClientCallback iWifiApClientCallback);

    public static WifiAp create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.wifiap.impl.WifiApImpl");
            Constructor c = clazz.getConstructor(Context.class);
            return (WifiAp) c.newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
