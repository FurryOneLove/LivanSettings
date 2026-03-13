package com.ecarx.xui.adaptapi.audio.audiofx;

import com.ecarx.xui.adaptapi.FunctionStatus;

/* loaded from: classes.dex */
public interface IFaderBalance {
    short getBalanceLevel();

    short[] getBalanceLevelRange();

    short getFaderLevel();

    short[] getFaderLevelRange();

    FunctionStatus isBalanceSupported();

    FunctionStatus isFaderSupported();

    void setBalanceLevel(short s);

    void setFaderLevel(short s);
}
