package com.ecarx.xui.adaptapi.tbox.tboxmessager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface ITBoxMessager {
    public static final int TBOX_MSG_DEFAULT = 0;
    public static final int TBOX_MSG_NAVI = 2;
    public static final int TBOX_MSG_TEXT = 1;

    public interface TBoxMessageCallback {
        void onTBoxMessageGet(Object obj, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TBoxMessageType {
    }

    INaviInfoFromTBox getNaviInfo();

    int getTBoxMsgType();

    void setTBoxMessageCallback(TBoxMessageCallback tBoxMessageCallback, int i);

    void unsetTBoxMessageCallback(TBoxMessageCallback tBoxMessageCallback);
}
