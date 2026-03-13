package com.ecarx.xui.adaptapi.policy;

/* loaded from: classes.dex */
public interface IStoragePolicy {
    public static final int MOUNT_TYPE_DISK = 1;
    public static final int MOUNT_TYPE_MTP = 3;
    public static final int MOUNT_TYPE_UNKNOWN = 0;
    public static final int MOUNT_TYPE_USB = 2;

    @Deprecated
    public static final int VOLUME_GKUI_PRIVATE_COMMON = 5;

    @Deprecated
    public static final int VOLUME_GKUI_PRIVATE_MAP = 4;

    @Deprecated
    public static final int VOLUME_GKUI_PRIVATE_VR_RES = 3;
    public static final int VOLUME_PRIVATE_COMMON = 5;
    public static final int VOLUME_PRIVATE_MAP = 4;
    public static final int VOLUME_PRIVATE_VR_RES = 3;
    public static final int VOLUME_USB_FLASH_DISK_1 = 1;
    public static final int VOLUME_USB_FLASH_DISK_2 = 2;
    public static final int VOLUME_USB_HOST_1 = 10;
    public static final int VOLUME_USB_HOST_2 = 11;
    public static final int VOLUME_USB_HOST_3 = 12;
    public static final int VOLUME_USB_HOST_4 = 13;

    public interface IUsbDeviceListener {
        void onReceiveUsbDeviceAction(@UsbActions String str, IUsbVolumeInfo iUsbVolumeInfo);
    }

    public interface IUsbVolumeInfo {
        String getFullPath();

        @MountType
        int getMountTypes();

        @UsbHostId
        int getUsbHostId();

        String getVolumeId();
    }

    public @interface MountType {
    }

    public @interface UsbActions {
    }

    public @interface UsbHostId {
    }

    public @interface VolumeType {
    }

    int getUsbHostCount();

    IUsbVolumeInfo[] getUsbHostVolumeInfos(@UsbHostId int i);

    String getVolumeFullPath(@VolumeType int i);

    String getVolumeName(@VolumeType int i);

    boolean registerUsbDeviceListener(IUsbDeviceListener iUsbDeviceListener);

    boolean unregisterUsbDeviceListener(IUsbDeviceListener iUsbDeviceListener);
}
