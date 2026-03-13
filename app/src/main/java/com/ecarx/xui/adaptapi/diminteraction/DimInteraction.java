package com.ecarx.xui.adaptapi.diminteraction;

import android.content.Context;
import android.util.Log;
import com.ecarx.xui.adaptapi.AdaptAPI;
import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public abstract class DimInteraction extends AdaptAPI {
    public static final int APP_TYPE_AMAP = 1;
    public static final int APP_TYPE_DEFAULT = 0;
    public static final int PARAMS_TYPE_FLT = 1;
    public static final int PARAMS_TYPE_INT = 0;
    public static final int PARAMS_TYPE_STR = 2;
    public static final int SHOW_PRESENTATION_ALWAYS = 2;
    public static final int SHOW_PRESENTATION_NAVI_ROUTE = 1;
    public static final int SHOW_PRESENTATION_NEVER = 3;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @interface AppType {
    }

    public interface IInteractionCallback {
        void onShowPresentationOptionChanged(int i);
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @interface ParamsType {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @interface ShowPresentationOption {
    }

    @Deprecated
    public abstract IClimateInteraction getClimateInteraction();

    public abstract IContactsInteraction getContactsInteraction();

    @VendorDefinition(author = "@ECARX", date = "2020-07-24", project = "ALL", requirement = "XQ2020072339282")
    public abstract IDimMenuInteraction getDimMenuInteraction();

    public abstract IMediaInteraction getMediaInteraction();

    public abstract INaviInteraction getNaviInteraction();

    public abstract IPhoneCallInteraction getPhoneCallInteraction();

    public abstract int getShowPresentationOption();

    public abstract int getSupportedRankingType();

    public abstract IVendorInteraction getVendorInteraction();

    public abstract IVrInteraction getVrInteraction();

    public abstract void registerInteractionCallback(IInteractionCallback iInteractionCallback);

    public abstract void setPresentationToDimSwitch(int i, String str, String str2, boolean z);

    public abstract void unregisterInteractionCallback(IInteractionCallback iInteractionCallback);

    public abstract void updateAvgFuleRanking(int i, String str);

    public static DimInteraction create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.dim.impl.DimInteractionImpl");
            Constructor c = clazz.getConstructor(Context.class);
            return (DimInteraction) c.newInstance(context);
        } catch (Exception e) {
            Log.e("INavigation", "new INavigation ex:", e);
            return null;
        }
    }
}
