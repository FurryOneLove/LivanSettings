package com.ecarx.xui.adaptapi.device.ext;

import com.ecarx.xui.adaptapi.VendorDefinition;
import com.ecarx.xui.adaptapi.device.ext.common.BtDevice;
import java.util.List;

/* loaded from: classes.dex */
public interface IBtExtension {
    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    boolean cancelBtDiscovery();

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    IA2dpExtension getA2dpExtension();

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    int getBtState();

    String getConnectedPhoneNumber();

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    int getHeadsetPower(BtDevice btDevice);

    @VendorDefinition(author = "@ECARX", date = "2021-06-25", project = "KX11")
    String getPSDBluetoothMacAddress();

    IPbapExtension getPbapExtension();

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    boolean isBtDiscovering();

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    boolean isBtEnabled();

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    boolean registerBtCallback(IBtExtensionCallback iBtExtensionCallback);

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    boolean reqBtPair(String str);

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    List<BtDevice> reqBtPairedDevices();

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    boolean reqBtUnpair(String str);

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    boolean setBtEnable(boolean z);

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    boolean startBtDiscovery();

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    boolean unregisterBtCallback(IBtExtensionCallback iBtExtensionCallback);
}
