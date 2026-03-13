package com.ecarx.xui.adaptapi.navigation.ehp.v2.profile;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IProfLongValue {
    public static final int PROF_LONG_TYPE_AVERAGE_SPEED = 17;
    public static final int PROF_LONG_TYPE_DESTINATION = 16;
    public static final int PROF_LONG_TYPE_EXTENDED_LANE = 10;
    public static final int PROF_LONG_TYPE_MERGE_LINK = 21;
    public static final int PROF_LONG_TYPE_RT_TRAFFIC_FLOW_SPEED = 18;
    public static final int PROF_LONG_TYPE_TRAFFIC_EVENT = 19;
    public static final int PROF_LONG_TYPE_TRAFFIC_SIGN = 8;
    public static final int PROF_LONG_TYPE_WEATHER_INFO = 20;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProfileType {
    }

    int getProfileType();

    int getValue();
}
