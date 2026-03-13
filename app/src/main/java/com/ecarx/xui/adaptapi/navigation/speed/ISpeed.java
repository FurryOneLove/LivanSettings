package com.ecarx.xui.adaptapi.navigation.speed;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface ISpeed {
    public static final int KEY_STRING_ELEC_LIMITED_SPEED_INFO = 1073741826;
    public static final int KEY_STRING_JCT_WAY_INFO = 1073741835;
    public static final int KEY_STRING_ROAD_CLASS_INFO = 1073741827;
    public static final int KEY_STRING_START = 1073741824;

    @Retention(RetentionPolicy.SOURCE)
    public @interface KeyType {
    }

    boolean isSpeedLimitEnabled();

    void registerSpeedCallback(ISpeedCallback iSpeedCallback);

    void setSpeedLimitingInfo(int i, ISpeedLimitingInfo iSpeedLimitingInfo);

    void unregisterSpeedCallback(ISpeedCallback iSpeedCallback);
}
