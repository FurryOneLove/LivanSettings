package com.ecarx.xui.adaptapi.binder;

/* loaded from: classes.dex */
public interface IConnectable {

    public interface IConnectWatcher {
        void onConnected();

        void onDisConnected();
    }

    void connect();

    void disconnect();

    void registerConnectWatcher(IConnectWatcher iConnectWatcher);

    void unregisterConnectWatcher();
}
