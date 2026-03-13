package com.ecarx.xui.adaptapi.dvr;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/* loaded from: classes.dex */
public interface IOperation {
    public static final int MODE_AUTO_STARTED_EMERGENCY_RECORDING = 16;
    public static final int MODE_BROWSE_EDIT = 16384;
    public static final int MODE_BROWSE_EMERGENCY_RECORD = 128;
    public static final int MODE_BROWSE_GENERAL_RECORD = 64;
    public static final int MODE_BROWSE_PHOTO = 512;
    public static final int MODE_BROWSE_RISK_RECORD = 256;
    public static final int MODE_COMMUNICATE_ERROR = 131072;
    public static final int MODE_EXIT_BROWSE_EDIT = 65536;
    public static final int MODE_GENERAL_RECORDING = 2;
    public static final int MODE_INITIALIZING = 1;
    public static final int MODE_PAUSE_RECORD = 4;
    public static final int MODE_RISK_RECORDING = 32;
    public static final int MODE_SETTING = 32768;
    public static final int MODE_SYSTEM_FAULT = 8192;
    public static final int MODE_UPDATE_MODE = 4096;
    public static final int MODE_USER_STARTED_EMERGENCY_RECORDING = 8;
    public static final int MODE_VIDEO_PAUSE = 2048;
    public static final int MODE_VIDEO_PLAYING = 1024;
    public static final int OPERATION_STATUS_FAILED = 2;
    public static final int OPERATION_STATUS_FILE_NOT_FOUND = 4;
    public static final int OPERATION_STATUS_IN_PROGRESS = 3;
    public static final int OPERATION_STATUS_NO_RESPONSE = 0;
    public static final int OPERATION_STATUS_NO_SPACE = 5;
    public static final int OPERATION_STATUS_OTHER = 6;
    public static final int OPERATION_STATUS_SUCCEEDED = 1;
    public static final int PLAY_STATUS_NONE = 0;
    public static final int PLAY_STATUS_PAUSED = 2;
    public static final int PLAY_STATUS_PLAYING = 1;
    public static final int PLAY_STATUS_STOP = 3;
    public static final int ROTATE_DIRECTION_ANTICLOCKWISE = 2;
    public static final int ROTATE_DIRECTION_CLOCKWISE = 1;
    public static final int SDCARD_STATUS_EMERGENCY_VIDEO_PARTITION_FULL = 6;
    public static final int SDCARD_STATUS_ERROR = 3;
    public static final int SDCARD_STATUS_FORMATTING = 8;
    public static final int SDCARD_STATUS_INSUFFICIENT_STORAGE = 5;
    public static final int SDCARD_STATUS_NORMAL = 1;
    public static final int SDCARD_STATUS_NOT_FORMATTED = 4;
    public static final int SDCARD_STATUS_NO_SDCARD = 2;
    public static final int SDCARD_STATUS_PHOTO_PARTITION_FULL = 7;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DvrMode {
    }

    public interface IOperationCallback {
        void onBackToHomeStatus(int i);

        void onBrowsingFiles(List<IDvrFile> list);

        void onBrowsingPageChanged(int i, int i2);

        void onCaptureStatus(int i, IDvrPhotoFile iDvrPhotoFile);

        void onChangeModeStatus(int i);

        void onDeleteAllFilesStatus(int i);

        void onDeleteFilesStatus(int i);

        void onLockFileStatus(IDvrFile iDvrFile, int i);

        void onMode(int i);

        void onMoveVideosToEmergencyVideoPartitionStatus(int i);

        void onPlayStatus(IDvrFile iDvrFile, int i);

        void onSdcardFormattingStatus(int i);

        void onSdcardStatus(int i);

        void onSwitchPageStatus(int i);

        void onUnlockFileStatus(IDvrFile iDvrFile, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OperationStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PlayStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RotateDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SdcardStatus {
    }

    boolean backToHome();

    boolean browseFirstPage();

    boolean browseLastPage();

    boolean browseNextPage();

    boolean browsePage(int i);

    boolean browsePreviousPage();

    boolean capture();

    boolean changeMode(int i);

    boolean deleteAllFiles();

    boolean deleteAllFilesWithType(int i);

    boolean deleteFiles(List<IDvrFile> list);

    boolean exitPlay();

    boolean formatSdcard();

    int getCurrentMode();

    int getPageCount();

    int getPlayingStatus();

    int getSdcardStatus();

    boolean isCameraOnline();

    boolean lockFile(IDvrFile iDvrFile);

    boolean moveVideosToEmergencyVideoPartition(List<IDvrVideoFile> list);

    boolean naviDown();

    boolean naviLeft();

    boolean naviRight();

    boolean naviUp();

    boolean pause();

    boolean play();

    boolean play(IDvrFile iDvrFile);

    boolean playNext();

    boolean playPrevious();

    boolean replay();

    boolean rotatePhoto(int i);

    boolean rotatePhoto(IDvrPhotoFile iDvrPhotoFile, int i);

    boolean selectAllFiles();

    boolean selectFile(IDvrFile iDvrFile);

    void setCallback(IOperationCallback iOperationCallback);

    boolean unlockFile(IDvrFile iDvrFile);

    void unsetCallback(IOperationCallback iOperationCallback);
}
