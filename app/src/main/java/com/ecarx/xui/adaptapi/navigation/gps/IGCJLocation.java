package com.ecarx.xui.adaptapi.navigation.gps;

import android.location.Location;
import com.ecarx.xui.adaptapi.FunctionStatus;

/* loaded from: classes.dex */
public interface IGCJLocation {
    FunctionStatus isGCJLocationSupported();

    void updateGCJLocation(Location location);
}
