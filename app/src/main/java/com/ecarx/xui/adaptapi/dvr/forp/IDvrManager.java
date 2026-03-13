package com.ecarx.xui.adaptapi.dvr.forp;

import com.ecarx.xui.adaptapi.FunctionStatus;
import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IDvrManager {
    public static final int APPLICATION_CAMERA = 1;
    public static final int APPLICATION_DEFAULT = 0;
    public static final int CAMERA_FRONT = 1;
    public static final int CAMERA_INNER = 2;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int DVR_STATE_360_VIEWS_MODULE_ERROR = 28;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int DVR_STATE_ACCELERATION_SENSOR_ABNORMAL = 22;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int DVR_STATE_CAMERA_CONNECTION_LOST = 24;
    public static final int DVR_STATE_CAPTURE_PIC = 6;
    public static final int DVR_STATE_EMERGENCY_RECORDING = 4;
    public static final int DVR_STATE_EMERGENCY_RECORDING_AUTO = 5;
    public static final int DVR_STATE_EMERGENCY_RECORDING_AUTO_ENDED = 10;
    public static final int DVR_STATE_EMERGENCY_RECORDING_ENDED = 9;
    public static final int DVR_STATE_FACTORY_RESETTING = 15;
    public static final int DVR_STATE_FACTORY_RESET_FAILED = 17;
    public static final int DVR_STATE_FACTORY_RESET_SUCCEED = 16;
    public static final int DVR_STATE_GENERAL_RECORDING = 2;
    public static final int DVR_STATE_INITIALIZING = 1;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int DVR_STATE_MEMORY_READ_ERROR = 26;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int DVR_STATE_MEMORY_WRITE_ERROR = 25;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int DVR_STATE_NETWORK_ABNORMAL = 29;
    public static final int DVR_STATE_NO_SPACE = 8;
    public static final int DVR_STATE_OFFLINE = 11;
    public static final int DVR_STATE_ONLINE = 12;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int DVR_STATE_OVER_HEAT = 19;
    public static final int DVR_STATE_PAUSE_RECORDING = 3;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int DVR_STATE_RESOURCE_OCCUPIED = 21;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int DVR_STATE_SYSTEM_ERROR = 18;
    public static final int DVR_STATE_UNKNOWN = 0;
    public static final int DVR_STATE_UPDATE_FAILED = 14;
    public static final int DVR_STATE_UPDATE_SUCCEED = 13;
    public static final int DVR_STATE_UPDATING = 7;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int DVR_STATE_VIDEO_RECORD_FAIL = 23;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int DVR_STATE_VIDEO_RECORD_FAILURE = 27;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int DVR_STATE_VOLTAGE_ABNORMAL = 20;
    public static final int ERROR_CODE_NOT_SUPPORTED = 5;
    public static final int ERROR_CODE_NO_RESPONSE = 3;
    public static final int ERROR_CODE_NO_SPACE = 4;
    public static final int ERROR_CODE_SYSTEM_BUSY = 1;
    public static final int ERROR_CODE_TIMEOUT = 2;
    public static final int ERROR_CODE_UNKNOWN = 0;
    public static final int OPERATION_CAPTURE_PIC = 4100;
    public static final int OPERATION_EMERGENCY_RECORDING = 4099;
    public static final int OPERATION_FACTORY_RESET = 4104;
    public static final int OPERATION_GENERAL_RECORDING = 4097;
    public static final int OPERATION_MUTE_MIC = 4102;
    public static final int OPERATION_PAUSE_RECORDING = 4098;
    public static final int OPERATION_SDCARD_FORMAT = 4101;
    public static final int OPERATION_STATUS_FAILED = 4;
    public static final int OPERATION_STATUS_PREPARE = 1;
    public static final int OPERATION_STATUS_PROGRESS = 2;
    public static final int OPERATION_STATUS_SUCCEED = 3;
    public static final int OPERATION_STATUS_UNKNOWN = 0;
    public static final int OPERATION_SWITCH_CAMERA = 4105;
    public static final int OPERATION_UNMUTE_MIC = 4103;
    public static final int SDCARD_STATE_UNKNOWN = 0;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int SDCARD_STATUS_EMERGENCY_AREA_OVER_THRESHOLD = 13;
    public static final int SDCARD_STATUS_EMERGENCY_VIDEO_PARTITION_FULL = 7;
    public static final int SDCARD_STATUS_ERROR = 3;
    public static final int SDCARD_STATUS_FORMATTING = 5;

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    public static final int SDCARD_STATUS_INITIALIZE_FAIL = 10;
    public static final int SDCARD_STATUS_NORMAL = 1;
    public static final int SDCARD_STATUS_NOT_FORMATTED = 4;
    public static final int SDCARD_STATUS_NO_SDCARD = 2;
    public static final int SDCARD_STATUS_NO_SPACE = 6;
    public static final int SDCARD_STATUS_PHOTO_PARTITION_FULL = 8;

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    public static final int SDCARD_STATUS_POOR_WRITE_PERFORMANCE = 11;

    @VendorDefinition(author = "@ECARX", date = "2021-02-19", project = "KX11", requirement = "")
    public static final int SDCARD_STATUS_PRIVATE_FILES_TAKE_TOO_MUCH_SPACE = 14;

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    public static final int SDCARD_STATUS_SDCARD_INCOMPATIBLE = 9;

    @VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
    public static final int SDCARD_STATUS_WRITE_FAIL = 12;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ApplicationType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraOperation {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DvrOperation {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DvrStates {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorCode {
    }

    public interface IDvrObserver {
        void onDvrStateChanged(int i);

        void onOperationError(int i, int i2);

        void onOperationStatus(int i, int i2);

        void onSDCardStateChanged(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OperationStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OperationType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SDCardStatus {
    }

    void doDvrCameraOperation(int i, int i2);

    void doDvrCameraOperation(int i, int i2, int i3);

    void doDvrOperation(int i);

    int getCurrentDvrStates();

    IFileManager getFileManager();

    int getSDCardStates();

    FunctionStatus isDvrCameraOperationSupported(int i, int i2);

    FunctionStatus isDvrCameraSupported(int i);

    FunctionStatus isDvrOperationSupported(int i);

    boolean registerOperationObserver(IDvrObserver iDvrObserver);

    boolean unegisterOperationObserver(IDvrObserver iDvrObserver);
}
