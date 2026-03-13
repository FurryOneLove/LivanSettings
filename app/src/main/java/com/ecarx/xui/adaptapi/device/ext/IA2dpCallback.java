package com.ecarx.xui.adaptapi.device.ext;

import com.ecarx.xui.adaptapi.VendorDefinition;

@VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
/* loaded from: classes.dex */
public interface IA2dpCallback {
    void onA2dpServiceReady();

    void onA2dpStateChanged(String str, int i, int i2);
}
