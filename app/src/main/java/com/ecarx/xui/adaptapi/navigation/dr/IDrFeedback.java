package com.ecarx.xui.adaptapi.navigation.dr;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IDrFeedback {
    public static final int TYPE_FEADBACK_BRIDGE = 16;
    public static final int TYPE_FEADBACK_ELEVATED = 8;
    public static final int TYPE_FEADBACK_INVALID = 0;
    public static final int TYPE_FEADBACK_MATCHED = 1;
    public static final int TYPE_FEADBACK_ROUND_ABOUT = 4;
    public static final int TYPE_FEADBACK_TUNNEL = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface FeadbackType {
    }

    public interface LocFeedbackNode {
        int getFeedbackType();

        int getLat();

        int getLon();

        float getProbability();

        float getRoadAZi();

        int getRoadWidth();

        int getZLevel();
    }

    int getCount();

    LocFeedbackNode[] getFeedbackNode();

    long getTicktime();

    double toRoadEndDist();

    double toRoadStartDist();
}
