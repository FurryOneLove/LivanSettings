package com.ecarx.xui.adaptapi.navigation.eco;

import android.os.Bundle;
import com.ecarx.xui.adaptapi.FunctionStatus;

/* loaded from: classes.dex */
public interface IECONavigation {

    public interface IRoadConditionInfo {
        int getCurrentRoadCondition();

        int getCurrentRoadCongestionLength();

        int getCurrentRoadCongestionLevel();

        int getCurrentRoadPassTime();

        int getDistanceToNextCongestionRoad();

        Bundle getExtendInformation();

        int getNavigationState();

        int getNextCongestionRoadLength();

        int getNextCongestionRoadPassTime();

        int getNextRoadCongestionLevel();
    }

    FunctionStatus isECONavigationSupported();

    void updateRoadConditionInfo(IRoadConditionInfo iRoadConditionInfo);
}
