package com.ecarx.xui.adaptapi.dvr.ota;

/* loaded from: classes.dex */
public interface IOtaCallback {
    void onFailed(int i);

    void onRebootingAfterOta();

    void onShouldBeginInstall();

    void onSucceeded();
}
