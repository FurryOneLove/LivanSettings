package com.ecarx.xui.adaptapi.navigation.ehp.v2;

import com.ecarx.xui.adaptapi.navigation.ehp.v2.profile.IProfLongValue;

/* loaded from: classes.dex */
public interface IHznProfLongMessage extends IV2Message {
    int getOffset();

    int getPathIndex();

    IProfLongValue getProfileValue();

    int isControlPoint();

    int isRetransmission();

    int isUpdate();
}
