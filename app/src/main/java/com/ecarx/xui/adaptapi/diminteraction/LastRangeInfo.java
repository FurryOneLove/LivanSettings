package com.ecarx.xui.adaptapi.diminteraction;

import android.os.Parcel;
import android.os.Parcelable;
import com.ecarx.xui.adaptapi.VendorDefinition;

@VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "EX11")
/* loaded from: classes.dex */
public class LastRangeInfo implements Parcelable {
    public static final Parcelable.Creator<LastRangeInfo> CREATOR = new Parcelable.Creator<LastRangeInfo>() { // from class: com.ecarx.xui.adaptapi.diminteraction.LastRangeInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LastRangeInfo createFromParcel(Parcel in) {
            return new LastRangeInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LastRangeInfo[] newArray(int size) {
            return new LastRangeInfo[size];
        }
    };
    private double latitudeE6;
    private double latitudeE61;
    private double longitudeE6;
    private double longitudeE61;
    private String msgSubTitle;
    private String name;

    public LastRangeInfo(double longitudeE6, double latitudeE6, double longitudeE61, double latitudeE61, String name, String msgSubTitle) {
        this.longitudeE6 = longitudeE6;
        this.latitudeE6 = latitudeE6;
        this.longitudeE61 = longitudeE61;
        this.latitudeE61 = latitudeE61;
        this.name = name;
        this.msgSubTitle = msgSubTitle;
    }

    protected LastRangeInfo(Parcel in) {
        this.longitudeE6 = in.readDouble();
        this.latitudeE6 = in.readDouble();
        this.longitudeE61 = in.readDouble();
        this.latitudeE61 = in.readDouble();
        this.name = in.readString();
        this.msgSubTitle = in.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.longitudeE6);
        dest.writeDouble(this.latitudeE6);
        dest.writeDouble(this.longitudeE61);
        dest.writeDouble(this.latitudeE61);
        dest.writeString(this.name);
        dest.writeString(this.msgSubTitle);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public double getLatitudeE6() {
        return this.latitudeE6;
    }

    public double getLongitudeE6() {
        return this.longitudeE6;
    }

    public double getLatitudeE61() {
        return this.latitudeE61;
    }

    public double getLongitudeE61() {
        return this.longitudeE61;
    }

    public String getMsgSubTitle() {
        return this.msgSubTitle;
    }

    public String getName() {
        return this.name;
    }
}
