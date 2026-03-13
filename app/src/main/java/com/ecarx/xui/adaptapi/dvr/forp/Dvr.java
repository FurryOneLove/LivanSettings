package com.ecarx.xui.adaptapi.dvr.forp;

import android.content.Context;
import com.ecarx.xui.adaptapi.AdaptAPI;
import com.ecarx.xui.adaptapi.dvr.ota.IOta;

/* loaded from: classes.dex */
public abstract class Dvr extends AdaptAPI {
    public abstract IDvrFunction getDvrFunction();

    public abstract IDvrInfo getDvrInfos();

    public abstract IDvrManager getDvrManager();

    public abstract IOta getOta();

    public static Dvr create(Context context) {
        return null;
    }
}
