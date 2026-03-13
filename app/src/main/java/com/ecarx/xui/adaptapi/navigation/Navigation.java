package com.ecarx.xui.adaptapi.navigation;

import android.content.Context;
import android.util.Log;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public class Navigation {
    public static INavigation create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.navigation.impl.INavigationImpl");
            Constructor c = clazz.getConstructor(Context.class);
            return (INavigation) c.newInstance(context);
        } catch (Exception e) {
            Log.e("INavigation", "new INavigation ex:", e);
            return null;
        }
    }
}
