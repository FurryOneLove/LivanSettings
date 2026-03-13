package com.ecarx.xui.adaptapi.ota;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IOtaSessionCallback {

    @VendorDefinition(author = "@ECARX", date = "2020-12-11", project = "ALL", requirement = "")
    public static final int UPDATE_FAILED_CHARGE_CONNECTED = 15;
    public static final int UPDATE_FAILED_CONFIG_ERROR = 6;

    @VendorDefinition(author = "@ECARX", date = "2020-10-12", project = "ALL", requirement = "")
    public static final int UPDATE_FAILED_CRITICAL_CONFIGURATION_MISMATCH_OR_OTHER = 14;
    public static final int UPDATE_FAILED_DOOR_LOCKING = 8;

    @VendorDefinition(author = "@ECARX", date = "2021-3-8", project = "FY11", requirement = "")
    public static final int UPDATE_FAILED_GEAR_POSITION = 22;
    public static final int UPDATE_FAILED_LOW_BATTERY = 4;
    public static final int UPDATE_FAILED_MEMORY_ERROR = 10;
    public static final int UPDATE_FAILED_NETWORK_ERROR = 3;

    @VendorDefinition(author = "@ECARX", date = "2021-3-8", project = "FY11", requirement = "")
    public static final int UPDATE_FAILED_POWER_ON_FAILED = 19;
    public static final int UPDATE_FAILED_REASON_DEFAULT = 0;
    public static final int UPDATE_FAILED_REASON_INSUFFICIENT_STORAGE = 2;
    public static final int UPDATE_FAILED_REASON_INVALID_PACKAGE = 1;
    public static final int UPDATE_FAILED_SERVICE_ERROR = 9;

    @VendorDefinition(author = "@ECARX", date = "2020-12-11", project = "ALL", requirement = "")
    public static final int UPDATE_FAILED_SYSTEM = 18;

    @VendorDefinition(author = "@ECARX", date = "2020-12-11", project = "ALL", requirement = "")
    public static final int UPDATE_FAILED_TEMPERATURE_LOW = 16;

    @VendorDefinition(author = "@ECARX", date = "2021-3-8", project = "FY11", requirement = "")
    public static final int UPDATE_FAILED_THEFT_DOOR = 23;

    @VendorDefinition(author = "@ECARX", date = "2021-3-8", project = "FY11", requirement = "")
    public static final int UPDATE_FAILED_THEFT_HOOD = 20;

    @VendorDefinition(author = "@ECARX", date = "2021-3-8", project = "FY11", requirement = "")
    public static final int UPDATE_FAILED_THEFT_TRUNK = 21;
    public static final int UPDATE_FAILED_TIME_OUT = 5;
    public static final int UPDATE_FAILED_UPDATING_ERROR = 11;

    @VendorDefinition(author = "@ECARX", date = "2020-08-24", project = "ALL", requirement = "XQ2020081271217")
    public static final int UPDATE_FAILED_VEHICLE_IN_USE = 12;

    @VendorDefinition(author = "@ECARX", date = "2020-08-24", project = "ALL", requirement = "XQ2020081271217")
    public static final int UPDATE_FAILED_VEHICLE_NOT_SECURED = 13;

    @VendorDefinition(author = "@ECARX", date = "2020-12-11", project = "ALL", requirement = "")
    public static final int UPDATE_FAILED_WINDOW = 17;

    @Retention(RetentionPolicy.SOURCE)
    public @interface UpdateFailedReason {
    }

    void onFailed(int i);

    void onProgressUpdate(int i);

    void onRebootingAfterOta();

    void onSessionCanceled();

    void onShouldBeginInstall();

    void onSucceeded();
}
