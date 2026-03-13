package com.ecarx.xui.adaptapi.tbox.ota;

/* loaded from: classes.dex */
public interface IOtaSession {
    boolean cancel();

    boolean couldBeginInstallRightNow();

    int getOtaProgress();

    boolean ifSystemWillRebootAfterOta();

    boolean isInstallationStarted();
}
