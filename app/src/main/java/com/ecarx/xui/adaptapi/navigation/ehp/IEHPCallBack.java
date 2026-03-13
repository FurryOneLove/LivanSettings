package com.ecarx.xui.adaptapi.navigation.ehp;

import com.ecarx.xui.adaptapi.VendorDefinition;

/* loaded from: classes.dex */
public interface IEHPCallBack {

    public interface GpsInfo {
        int getBiasTiForMins();

        int getBiasTiForMsec();

        int getBiasTiForSec();

        double getLatitude();

        double getLongitude();

        int getOriTiForMins();

        int getOriTiForMsec();

        int getOriTiForSec();

        int getUTCForDay();

        int getUTCForHr();

        int getUTCForMins();

        int getUTCForMth();

        int getUTCForSec();

        int getUTCForYr();
    }

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "EX11")
    void onGpsInfoChange(GpsInfo gpsInfo);
}
