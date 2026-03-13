package com.ecarx.xui.adaptapi.device.ext.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.ecarx.xui.adaptapi.VendorDefinition;

@VendorDefinition(author = "@ECARX", date = "2021-01-18", project = "KX11", requirement = "")
/* loaded from: classes.dex */
public class BtDevice implements Parcelable {
    public static final Parcelable.Creator<BtDevice> CREATOR = new Parcelable.Creator<BtDevice>() { // from class: com.ecarx.xui.adaptapi.device.ext.common.BtDevice.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BtDevice[] newArray(int size) {
            return new BtDevice[size];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BtDevice createFromParcel(Parcel source) {
            BtDevice btDevice = new BtDevice();
            btDevice.address = source.readString();
            btDevice.name = source.readString();
            btDevice.supportProfile = source.readInt();
            btDevice.category = source.readInt();
            btDevice.bondState = source.readInt();
            btDevice.connectState = source.readInt();
            return btDevice;
        }
    };
    private static final String TAG = "BtDevice";
    private String address;
    private String name;
    private int supportProfile = 0;
    private int category = 0;
    private int bondState = 0;
    private int connectState = 0;

    public BtDevice() {
    }

    public BtDevice(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSupportProfile() {
        return this.supportProfile;
    }

    public void setSupportProfile(int supportProfile) {
        this.supportProfile = supportProfile;
    }

    public int getCategory() {
        return this.category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getBondState() {
        return this.bondState;
    }

    public void setBondState(int bondState) {
        this.bondState = bondState;
    }

    public int getConnectState() {
        return this.connectState;
    }

    public void setConnectState(int connectState) {
        this.connectState = connectState;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.name);
        dest.writeInt(this.supportProfile);
        dest.writeInt(this.category);
        dest.writeInt(this.bondState);
        dest.writeInt(this.connectState);
    }
}
