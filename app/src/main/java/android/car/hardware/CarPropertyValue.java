package android.car.hardware;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint({"ParcelCreator"})
/* loaded from: classes.dex */
public class CarPropertyValue<T> implements Parcelable {
    public CarPropertyValue(Parcel parcel) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        throw new RuntimeException("Stub!");
    }

    public int getPropertyId() {
        throw new RuntimeException("Stub!");
    }

    public int getAreaId() {
        throw new RuntimeException("Stub!");
    }

    public int getStatus() {
        throw new RuntimeException("Stub!");
    }

    public long getTimestamp() {
        throw new RuntimeException("Stub!");
    }

    public T getValue() {
        throw new RuntimeException("Stub!");
    }
}
