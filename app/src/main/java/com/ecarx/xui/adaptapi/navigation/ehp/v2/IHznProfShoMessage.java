package com.ecarx.xui.adaptapi.navigation.ehp.v2;

import com.ecarx.xui.adaptapi.navigation.ehp.v2.profile.IProfShortValue;

/* loaded from: classes.dex */
public interface IHznProfShoMessage extends IV2Message {
    int Offset();

    int getAccuracy();

    int getDistance1();

    int getPathIndex();

    IProfShortValue getValue0();

    int getValue1();

    int isControlPoint();

    int isRetransmission();

    int isUpdate();
}
