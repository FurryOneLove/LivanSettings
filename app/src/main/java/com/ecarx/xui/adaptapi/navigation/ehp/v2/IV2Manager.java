package com.ecarx.xui.adaptapi.navigation.ehp.v2;

import com.ecarx.xui.adaptapi.FunctionStatus;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IV2Manager {
    public static final int V2_REQUEST_LONG_MSG_START = 1;
    public static final int V2_REQUEST_LONG_MSG_STOP = 2;

    public interface IV2ProviderListener {
        void onEHPProviderAvailable(FunctionStatus functionStatus);

        void onProviderRequested(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface V2Request {
    }

    FunctionStatus isV2ProviderAvailable();

    boolean registerV2ProviderListener(IV2ProviderListener iV2ProviderListener);

    boolean unregisterV2ProviderListener(IV2ProviderListener iV2ProviderListener);

    int updadteHznMessage(IV2Message iV2Message);
}
