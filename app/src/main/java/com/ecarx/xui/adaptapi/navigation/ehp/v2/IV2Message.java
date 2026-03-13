package com.ecarx.xui.adaptapi.navigation.ehp.v2;

import android.os.Bundle;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IV2Message {
    public static final int LONG_RANGE_MESSAGE = 4096;
    public static final int MESSAGE_TYPE_HZN_DATA = 6;
    public static final int MESSAGE_TYPE_HZN_EDGE = 3;
    public static final int MESSAGE_TYPE_HZN_POSITION = 1;
    public static final int MESSAGE_TYPE_HZN_POSITION_LR = 4097;
    public static final int MESSAGE_TYPE_HZN_PROFLONG = 5;
    public static final int MESSAGE_TYPE_HZN_PROFLONG_LR = 4101;
    public static final int MESSAGE_TYPE_HZN_PROFSHORT = 4;
    public static final int MESSAGE_TYPE_HZN_SEGMENT = 2;
    public static final int MESSAGE_TYPE_HZN_STATUS = 0;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageType {
    }

    int getCyclicCounter();

    Bundle getExtendValue();

    int getMessageType();
}
