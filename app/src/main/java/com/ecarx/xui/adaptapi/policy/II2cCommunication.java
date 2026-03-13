package com.ecarx.xui.adaptapi.policy;

/* loaded from: classes.dex */
public interface II2cCommunication {
    boolean isReady();

    boolean registerI2cListener(I2cCallback i2cCallback);

    void setData(int[] iArr);

    void setOnReadyCallback(I2cReadyCallback i2cReadyCallback);
}
