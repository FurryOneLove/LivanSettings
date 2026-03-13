package com.ecarx.xui.adaptapi.ota;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;

/* loaded from: classes.dex */
public interface IOtaSession {
    public static final int OTA_MODE_DOWNLOAD_INSTALL = 2;
    public static final int OTA_MODE_INSTALL_DIRECTLY = 1;
    public static final int OTA_MODE_SELF_DOWNLOAD_INSTALL = 3;
    public static final int OTA_PRIORITY_HIGH = 2;
    public static final int OTA_PRIORITY_LOW = 0;
    public static final int OTA_PRIORITY_NORMAL = 1;
    public static final int OTA_PRIORITY_SET_TIME = 3;
    public static final int OTA_PRIORITY_UNKNOWN = 0;

    @VendorDefinition(author = "@ECARX", date = "2020-08-24", project = "KX11-FY11", requirement = "XQ2020081271217")
    public static final int OTA_UPDATE_INPROGRESS_STATE_IDLE = 1;

    @VendorDefinition(author = "@ECARX", date = "2020-08-24", project = "KX11-FY11", requirement = "XQ2020081271217")
    public static final int OTA_UPDATE_INPROGRESS_STATE_UPGRADE = 2;

    @VendorDefinition(author = "@ECARX", date = "2021-01-26", project = "KX11", requirement = "")
    public static final int REGRET_TERMINATE = 2;

    @VendorDefinition(author = "@ECARX", date = "2021-01-26", project = "KX11", requirement = "")
    public static final int REGRET_TIMEOUT = 1;

    @VendorDefinition(author = "@ECARX", date = "2021-01-26", project = "KX11", requirement = "")
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface InstallRegretState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OtaMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OtaPriority {
    }

    @VendorDefinition(author = "@ECARX", date = "2020-08-24", project = "KX11-FY11", requirement = "XQ2020081271217")
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface OtaUpdateInProgressState {
    }

    boolean cancel();

    @VendorDefinition(author = "@ECARX", date = "2020-08-24", project = "ALL", requirement = "XQ2020081271217")
    boolean cancelOtaUpgradeTime();

    boolean checkUpdate();

    boolean couldBeginInstallRightNow();

    boolean download();

    int getEstimatedInstallationTime();

    int getOtaBaseSysVersionCode();

    String getOtaBaseSysVersionName();

    int getOtaMode();

    int getOtaPriority();

    int getOtaProgress();

    int getOtaType();

    @VendorDefinition(author = "@ECARX", date = "2020-12-11", project = "ALL", requirement = "")
    int getOtaUpdateInProgressState();

    Calendar getOtaUpdateTime();

    int getSysVersionCode();

    String getSysVersionName();

    @VendorDefinition(author = "@ECARX", date = "2020-08-24", project = "ALL", requirement = "XQ2020081271217")
    String getUpgradeInfo();

    boolean ifSystemWillRebootAfterOta();

    boolean isInstallationStarted();

    @VendorDefinition(author = "@ECARX", date = "2020-12-11", project = "ALL", requirement = "")
    boolean isPopupEnable();

    boolean isRecoveryOta();

    @VendorDefinition(author = "@ECARX", date = "2021-01-26", project = "KX11", requirement = "")
    void setInstallRegretState(int i);

    boolean setOtaUpdateTime(Calendar calendar);

    @VendorDefinition(author = "@ECARX", date = "2020-08-24", project = "KX11-FY11", requirement = "XQ2020081271217")
    boolean setPowerState(int i);
}
