package com.ecarx.xui.adaptapi.vehicle;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@VendorDefinition(author = "@ECARX", date = "2021-07-23", project = "lambda", requirement = "")
/* loaded from: classes.dex */
public final class VehicleVent {
    public static final int VENT_ROW_1_LEFT_CENTER = 2;
    public static final int VENT_ROW_1_LEFT_SIDE = 1;
    public static final int VENT_ROW_1_RIGHT_CENTER = 4;
    public static final int VENT_ROW_1_RIGHT_SIDE = 8;
    public static final int VENT_ROW_2_LEFT = 16;
    public static final int VENT_ROW_2_RIGHT = 64;
    public static final int VENT_ROW_3_LEFT = 256;
    public static final int VENT_ROW_3_RIGHT = 1024;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeatType {
    }

    private VehicleVent() {
    }
}
