package com.ecarx.xui.adaptapi.diminteraction;

import android.os.Parcel;
import android.os.Parcelable;
import com.ecarx.xui.adaptapi.VendorDefinition;

@VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "EX11")
/* loaded from: classes.dex */
public class NaviStatus implements Parcelable {
    public static final Parcelable.Creator<NaviStatus> CREATOR = new Parcelable.Creator<NaviStatus>() { // from class: com.ecarx.xui.adaptapi.diminteraction.NaviStatus.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NaviStatus createFromParcel(Parcel in) {
            return new NaviStatus(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NaviStatus[] newArray(int size) {
            return new NaviStatus[size];
        }
    };
    private boolean isYawing;
    private int status;

    public NaviStatus(int status, boolean isYawing) {
        this.status = status;
        this.isYawing = isYawing;
    }

    protected NaviStatus(Parcel in) {
        this.status = in.readInt();
        this.isYawing = in.readInt() == 1;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.status);
        parcel.writeInt(this.isYawing ? 1 : 0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getStatus() {
        return this.status;
    }

    public boolean isYawing() {
        return this.isYawing;
    }
}
