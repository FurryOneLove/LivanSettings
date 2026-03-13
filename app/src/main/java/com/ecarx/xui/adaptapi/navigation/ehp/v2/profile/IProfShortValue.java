package com.ecarx.xui.adaptapi.navigation.ehp.v2.profile;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IProfShortValue {
    public static final int PROF_SHORT_TYPE_CURVATURE = 1;
    public static final int PROF_SHORT_TYPE_HEADING_CHANGE = 8;
    public static final int PROF_SHORT_TYPE_ROAD_CONDITION = 6;
    public static final int PROF_SHORT_TYPE_SLOPE = 3;
    public static final int PROF_SHORT_TYPE_SPEED_SIGN_POSITION = 7;
    public static final int PROF_SHORT_TYPE_TRAFFIC_FLOW = 19;
    public static final int PROF_SHORT_TYPE_TRAVEL_SPEED = 18;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProfileType {
    }

    int getProfileType();

    int getValue();
}
