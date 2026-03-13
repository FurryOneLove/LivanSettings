package com.ecarx.xui.adaptapi.car.base;

import com.ecarx.xui.adaptapi.CallStatus;
import com.ecarx.xui.adaptapi.car.base.ICarFunction;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IProFunction extends ICarFunction {

    public interface IPropertyWatcher extends ICarFunction.IFunctionValueWatcher {
        <E> void onPropertyChanged(IProValue<E> iProValue);
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface PropertyId {
    }

    <E> IProValue<E> getProperty(int i);

    <E> IProValue<E> getProperty(int i, int i2);

    <E> CallStatus setProperty(IProValue<E> iProValue);
}
