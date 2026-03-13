package com.ecarx.xui.adaptapi.tbox.tboxmessager;

import com.ecarx.xui.adaptapi.NonNull;

/* loaded from: classes.dex */
public interface INaviInfoFromTBox {
    @NonNull
    String getLat();

    @NonNull
    String getLon();

    String getMessageId();

    String getMessageTitle();

    String getSender();
}
