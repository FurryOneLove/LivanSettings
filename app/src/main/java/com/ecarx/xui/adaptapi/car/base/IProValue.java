package com.ecarx.xui.adaptapi.car.base;

import com.ecarx.xui.adaptapi.FunctionStatus;

/* loaded from: classes.dex */
public interface IProValue<T> {
    int getPropertyId();

    FunctionStatus getStatus();

    T getValue();

    int getZoneId();
}
