package com.ecarx.xui.adaptapi.car;

import android.content.Context;
import com.ecarx.xui.adaptapi.AdaptAPI;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public final class Car extends AdaptAPI {
    public static ICar create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.car.impl.CarImpl");
            Constructor c = clazz.getConstructor(Context.class);
            return (ICar) c.newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
