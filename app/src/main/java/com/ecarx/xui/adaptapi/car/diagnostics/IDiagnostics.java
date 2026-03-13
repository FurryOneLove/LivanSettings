package com.ecarx.xui.adaptapi.car.diagnostics;

/* loaded from: classes.dex */
public interface IDiagnostics {
    IBtDebug getBtDebug();

    IDiagnosticMonitor getDiagMonitor();

    IDtcManager getDtcManager();

    IPartInfos getPartInfoManager();

    IShCommand getShCommandManager();
}
