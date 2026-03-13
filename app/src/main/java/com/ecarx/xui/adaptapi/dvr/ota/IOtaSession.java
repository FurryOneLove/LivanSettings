package com.ecarx.xui.adaptapi.dvr.ota;

import com.ecarx.xui.adaptapi.FutureFeature;

@FutureFeature
/* loaded from: classes.dex */
public interface IOtaSession {
    int getOtaProgress();

    boolean ifSystemWillRebootAfterOta();
}
