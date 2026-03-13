package com.ecarx.xui.adaptapi.peripherals.wear;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IIntelligentKey {
    public static final int STATUS_CONNECTED = 2;
    public static final int STATUS_CONNECTING = 1;
    public static final int STATUS_DISCONNECT = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ConnectionState {
    }

    public interface IIntelligentKeyConnectionStateCallback {
        void onStateChanged(int i, int i2);
    }

    int getConnectionState();

    void registerIntelligentKeyConnectionStateCallback(IIntelligentKeyConnectionStateCallback iIntelligentKeyConnectionStateCallback);

    void unRegisterIntelligentKeyConnectionStateCallback(IIntelligentKeyConnectionStateCallback iIntelligentKeyConnectionStateCallback);
}
