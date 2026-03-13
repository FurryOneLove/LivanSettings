package com.ecarx.xui.adaptapi.dvr;

import android.graphics.Bitmap;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IDvrFile {
    public static final int FILE_TYPE_EMERGENCY_VIDEO = 1;
    public static final int FILE_TYPE_NORMAL_VIDEO = 2;
    public static final int FILE_TYPE_PHOTO = 4;

    @Retention(RetentionPolicy.SOURCE)
    public @interface FileType {
    }

    File getFile();

    String getId();

    Bitmap getThumbnail();

    int getType();

    boolean isFinalDvrFile();

    boolean isFristDvrFile();

    boolean isLocked();

    boolean isSelected();
}
