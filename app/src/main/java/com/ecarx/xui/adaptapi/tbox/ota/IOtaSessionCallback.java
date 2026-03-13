package com.ecarx.xui.adaptapi.tbox.ota;

/* loaded from: classes.dex */
public interface IOtaSessionCallback {
    void onBootCompleted();

    void onFailed(int i);

    void onRebootingAfterOta();

    void onSessionCanceled();

    void onShouldBeginInstall();

    void onSucceeded();
}
