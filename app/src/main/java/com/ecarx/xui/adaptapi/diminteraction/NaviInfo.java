package com.ecarx.xui.adaptapi.diminteraction;

import android.os.Parcel;
import android.os.Parcelable;
import com.ecarx.xui.adaptapi.VendorDefinition;

@VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "EX11")
/* loaded from: classes.dex */
public class NaviInfo implements Parcelable {
    public static final Parcelable.Creator<NaviInfo> CREATOR = new Parcelable.Creator<NaviInfo>() { // from class: com.ecarx.xui.adaptapi.diminteraction.NaviInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NaviInfo createFromParcel(Parcel in) {
            return new NaviInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NaviInfo[] newArray(int size) {
            return new NaviInfo[size];
        }
    };
    private String extra;
    private double lat;
    private double lng;
    private String name;
    private long remainDistance;
    private long remainTime;

    public NaviInfo(String name, double lat, double lng, long remainDistance, long remainTime) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.remainDistance = remainDistance;
        this.remainTime = remainTime;
    }

    protected NaviInfo(Parcel in) {
        this.name = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.remainDistance = in.readLong();
        this.remainTime = in.readLong();
        this.extra = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeLong(this.remainDistance);
        dest.writeLong(this.remainTime);
        dest.writeString(this.extra);
    }

    public String getName() {
        return this.name;
    }

    public double gatLatitude() {
        return this.lat;
    }

    public double gatLongitude() {
        return this.lng;
    }

    public long getRemainDistance() {
        return this.remainDistance;
    }

    public long getRemainTime() {
        return this.remainTime;
    }

    public String getExtra() {
        return this.extra;
    }
}
