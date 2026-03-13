package com.ecarx.xui.adaptapi.policy;

import android.content.Context;
import android.util.Log;
import com.ecarx.xui.adaptapi.AdaptAPI;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public abstract class Policy extends AdaptAPI {
    public abstract IAudioAttributes getAudioAttributes();

    @Deprecated
    public abstract IAudioPolicy getAudioPolicy();

    public abstract II2cCommunication getI2cCommunication();

    public abstract IStoragePolicy getStoragePolicy();

    public abstract IVoiceAssistantPolicy getVoiceAssistantPolicy();

    public abstract IWindowManagerPolicy getWindowManagerPolicy();

    public static Policy create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.policy.impl.PolicyImpl");
            Constructor c = clazz.getConstructor(Context.class);
            return (Policy) c.newInstance(context);
        } catch (Exception e) {
            Log.e("Device", "new Device ex:", e);
            e.printStackTrace();
            return null;
        }
    }
}
