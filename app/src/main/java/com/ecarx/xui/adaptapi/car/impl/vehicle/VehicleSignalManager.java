package com.ecarx.xui.adaptapi.car.impl.vehicle;

import android.car.hardware.property.CarPropertyManager;
import com.ecarx.xui.adaptapi.binder.IConnectable;

/* loaded from: classes.dex */
public class VehicleSignalManager implements IConnectable {
    public static final int CMD_AJMS_SOUND = 109;
    public static final int CMD_LOCAL_AUDIO_BLANCE = 108;
    public static final int CMD_LOCAL_VEHICLE_TYPE = 105;
    public static final int CMD_LOCAL_VIRTUAL_SURROUND = 107;
    public static final int CMD_MMI_AMPLIFIER_CONFIG = 101;
    public static final int CMD_MMI_AVM_CONFIG = 103;
    public static final int CMD_MMI_FOTA_CONFIG = 110;
    public static final int CMD_MMI_KEY_LIGHT_TYPE = 100;
    public static final int CMD_MMI_RVC_CONFIG = 102;
    public static final int CMD_MMI_STEERING_WHEEL_HEATING = 104;
    public static final int CMD_MMI_TBOX_CONFIG = 106;
    public static final int CMD_SUNROOF_HALF_OPEN = 111;
    public static final int CMD_TPMS_INDIRECT = 112;

    public boolean isConnected() {
        throw new RuntimeException("Stub!");
    }

    public CarPropertyManager getCarPropertyManager() {
        throw new RuntimeException("Stub!");
    }

    @Override // com.ecarx.xui.adaptapi.binder.IConnectable
    public void connect() {
        throw new RuntimeException("Stub!");
    }

    @Override // com.ecarx.xui.adaptapi.binder.IConnectable
    public void disconnect() {
        throw new RuntimeException("Stub!");
    }

    @Override // com.ecarx.xui.adaptapi.binder.IConnectable
    public void registerConnectWatcher(IConnectable.IConnectWatcher iConnectWatcher) {
        throw new RuntimeException("Stub!");
    }

    @Override // com.ecarx.xui.adaptapi.binder.IConnectable
    public void unregisterConnectWatcher() {
        throw new RuntimeException("Stub!");
    }
}
