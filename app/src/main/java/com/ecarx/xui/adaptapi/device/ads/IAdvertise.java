package com.ecarx.xui.adaptapi.device.ads;

import com.ecarx.xui.adaptapi.NonNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IAdvertise {
    public static final int OPERATION_CLICKED = 2;
    public static final int OPERATION_DISPLAY = 1;
    public static final int OPERATION_END = 4;
    public static final int OPERATION_SKIPPED = 3;

    public interface OnAdOperationChangedListener {
        void onOperationChanged(@NonNull String str, int i);

        void onUpdateAdRecord(IAdRecordInfo iAdRecordInfo);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OperationCode {
    }

    void addOnAdOperationChangedListener(@NonNull OnAdOperationChangedListener onAdOperationChangedListener);

    IAdRecordInfo getLatestAdRecord();

    void removeOnAdOperationChangedListener(@NonNull OnAdOperationChangedListener onAdOperationChangedListener);

    void setBootAdInfo(IBootAdInfo iBootAdInfo);
}
