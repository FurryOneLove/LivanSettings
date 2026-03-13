package com.ecarx.xui.adaptapi.navigation.ehp.v2;

/* loaded from: classes.dex */
public interface IHznEdgeMessage extends IV2Message {
    int getComplexIntersection();

    int getFormOfWay();

    int getFunctionalRoadClass();

    int getNumberOfLanesInDrivingDirection();

    int getNumberOfLanesInOppositeDirection();

    int getOffset();

    int getPartOfCalculatedRoute();

    int getPathIndex();

    int getRelativeProbability();

    int getRightofWay();

    int getSubPathIndex();

    int getTurnAngle();

    int isLastStubAtOffset();

    int isRetransmission();

    int isUpdate();
}
