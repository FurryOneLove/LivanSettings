package com.ecarx.xui.adaptapi.ota;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IOtaSessionNotification extends IOtaSessionCallback {
    public static final int NOTIFICATION_DOWNLOADING = 17;
    public static final int NOTIFICATION_DOWNLOAD_COMPLETED = 18;
    public static final int NOTIFICATION_DOWNLOAD_ERROR = 19;
    public static final int NOTIFICATION_ESTIMATED_TIME_UPDATE = 65;
    public static final int NOTIFICATION_INSTALLING = 33;

    @VendorDefinition(author = "@ECARX", date = "2020-12-11", project = "ALL", requirement = "")
    public static final int NOTIFICATION_INSTALL_ABORTED = 34;
    public static final int NOTIFICATION_NEW_VERSION = 1;

    @VendorDefinition(author = "@ECARX", date = "2021-02-26", project = "FY11", requirement = "")
    public static final int NOTIFICATION_REMIND_POPUP_ENABLE = 35;

    @VendorDefinition(author = "@ECARX", date = "2021-4-22", project = "KX11/EX11")
    public static final int NOTIFICATION_RESET = 81;
    public static final int NOTIFICATION_TIME_OVERDUE = 52;
    public static final int NOTIFICATION_TIME_REMIND = 51;
    public static final int NOTIFICATION_TIME_SET_FAILED = 50;
    public static final int NOTIFICATION_TIME_SET_SUCCEED = 49;

    @Retention(RetentionPolicy.SOURCE)
    public @interface NotificationType {
    }

    void onNotificationUpdate(int i);
}
