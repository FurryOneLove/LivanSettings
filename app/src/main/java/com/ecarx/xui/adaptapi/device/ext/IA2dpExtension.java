package com.ecarx.xui.adaptapi.device.ext;

import com.ecarx.xui.adaptapi.VendorDefinition;

@VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
/* loaded from: classes.dex */
public interface IA2dpExtension {
    String getA2dpConnectedAddress();

    int getA2dpConnectionState();

    int getA2dpLocalVolume(String str);

    int getA2dpThresholdVolume(String str);

    boolean isA2dpConnected();

    boolean isA2dpServiceReady();

    void muteA2dpAudio(boolean z, String str);

    void pauseA2dpRender();

    boolean registerA2dpCallback(IA2dpCallback iA2dpCallback);

    boolean reqA2dpConnect(String str);

    boolean reqA2dpDisconnect(String str);

    boolean setA2dpLocalVolume(int i);

    void startA2dpRender();

    boolean unregisterA2dpCallback(IA2dpCallback iA2dpCallback);
}
