package com.ecarx.xui.adaptapi.tbox;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.SignalStrength;
import android.util.Log;
import com.ecarx.xui.adaptapi.AdaptAPI;
import com.ecarx.xui.adaptapi.tbox.journallog.IJournalLog;
import com.ecarx.xui.adaptapi.tbox.ota.IOta;
import com.ecarx.xui.adaptapi.tbox.tboxmessager.ITBoxMessager;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public abstract class TBox extends AdaptAPI {
    public abstract CellInfo getCellInfo();

    public abstract String getHardwareVersion();

    public abstract String getICCID();

    public abstract IJournalLog getIJournalLog();

    public abstract String getIMEI();

    public abstract String getIMSI();

    public abstract String getLineNumber();

    public abstract String getMSISDN();

    public abstract String getNetworkOperator();

    public abstract String getNetworkOperatorName();

    public abstract String getNetworkType();

    public abstract IOta getOta();

    public abstract SignalStrength getSignalStrength();

    public abstract String getSoftwareVersion();

    public abstract int getSoftwareVersionCode();

    public abstract String getTBoxID();

    public abstract ITBoxMessager getTBoxMessager();

    public abstract ITBoxSettings getTBoxSettings();

    public abstract String getTBoxSupplierCode();

    public abstract boolean isSubscribed();

    public abstract boolean setTBoxReset();

    public static TBox create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.tbox.impl.TBoxImpl");
            Constructor c = clazz.getConstructor(Context.class);
            return (TBox) c.newInstance(context);
        } catch (Exception e) {
            Log.e("TBox", "new TBox ex:", e);
            return null;
        }
    }
}
