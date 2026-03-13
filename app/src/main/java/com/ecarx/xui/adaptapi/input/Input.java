package com.ecarx.xui.adaptapi.input;

import android.content.Context;
import android.util.Log;
import com.ecarx.xui.adaptapi.AdaptAPI;
import com.ecarx.xui.adaptapi.FunctionStatus;
import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public abstract class Input extends AdaptAPI {
    public abstract boolean abandonKeysInterception(IKeyCallback iKeyCallback);

    @VendorDefinition(author = "@ECARX", date = "2020-10-31", project = "ALL")
    public abstract IInputSettings getInputSettings();

    @VendorDefinition(author = "@ECARX", date = "2020-10-31", project = "ALL")
    public abstract FunctionStatus isInputSettingsSupported();

    public abstract int[] requestKeysInterception(int[] iArr, IKeyCallback iKeyCallback);

    public static Input create(Context context) {
        try {
            Log.d("AdapterAPI.Input", "new Input class");
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.input.impl.InputImpl");
            Constructor c = clazz.getConstructor(Context.class);
            return (Input) c.newInstance(context);
        } catch (Exception e) {
            Log.e("AdapterAPI.Input", "new Input ex:", e);
            return null;
        }
    }
}
