package com.ecarx.xui.adaptapi.navigation;

import com.ecarx.xui.adaptapi.navigation.dr.IDrAutoByFwk;
import com.ecarx.xui.adaptapi.navigation.dr.IDrAutoByMap;
import com.ecarx.xui.adaptapi.navigation.eco.IECONavigation;
import com.ecarx.xui.adaptapi.navigation.ehp.IEHP;
import com.ecarx.xui.adaptapi.navigation.gps.IGCJLocation;
import com.ecarx.xui.adaptapi.navigation.speed.ISpeed;

/* loaded from: classes.dex */
public interface INavigation {
    IDrAutoByFwk getDrAutoByFwk();

    @Deprecated
    IDrAutoByMap getDrAutoByMap();

    IECONavigation getECONavigation();

    IEHP getEHPManager();

    IGCJLocation getGCJLocation();

    ISpeed getSpeedInfo();
}
