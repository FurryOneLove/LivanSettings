package com.ecarx.xui.adaptapi.uiinteraction;

import android.content.Context;
import android.util.Log;
import com.ecarx.xui.adaptapi.AdaptAPI;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class UiInteraction extends AdaptAPI {
    public static IUiInteraction create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.uiinteraction.impl.UiInteractionImpl");
            Method method = clazz.getMethod("create", Context.class);
            return (IUiInteraction) method.invoke(null, context);
        } catch (Exception e) {
            Log.e("UiInteraction", "new UiInteraction ex:", e);
            e.printStackTrace();
            return null;
        }
    }
}
