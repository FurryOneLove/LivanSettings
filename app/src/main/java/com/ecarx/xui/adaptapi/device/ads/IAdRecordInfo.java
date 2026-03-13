package com.ecarx.xui.adaptapi.device.ads;

/* loaded from: classes.dex */
public interface IAdRecordInfo {
    String getAdId();

    int getClickCount();

    long getDisplayDuration();

    long getDisplayTimestamp();

    boolean isSkiped();
}
