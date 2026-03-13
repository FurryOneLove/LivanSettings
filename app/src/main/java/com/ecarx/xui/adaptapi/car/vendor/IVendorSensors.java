package com.ecarx.xui.adaptapi.car.vendor;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@VendorDefinition(author = "@ECARX", date = "2020-07-02", project = "ALL")
/* loaded from: classes.dex */
public interface IVendorSensors {
    public static final int VENDOR_SENSOR_TYPE = 10485760;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface VendorContinuousSensor {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface VendorEventSensor {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface VendorSensorEvents {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface VendorSensorGroup {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface VendorSensorRate {
    }
}
