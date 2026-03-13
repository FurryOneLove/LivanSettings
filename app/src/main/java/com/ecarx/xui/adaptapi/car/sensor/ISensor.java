package com.ecarx.xui.adaptapi.car.sensor;

import com.ecarx.xui.adaptapi.FunctionStatus;
import com.ecarx.xui.adaptapi.VendorDefinition;
import com.ecarx.xui.adaptapi.car.sensor.ISensorGroup;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface ISensor {
    public static final int RATE_DR = 16;
    public static final int RATE_FAST = 1;
    public static final int RATE_FASTEST = 0;
    public static final int RATE_NORMAL = 3;
    public static final int RATE_SLOW = 4;
    public static final int RATE_SLOWEST = 5;
    public static final int RATE_UI = 2;
    public static final int SENSOR_TYPE_ABS = 2101504;
    public static final int SENSOR_TYPE_ACCELERATOR_DEPTH = 1053696;
    public static final int SENSOR_TYPE_AIRBAG_STATUS_DRIVER = 2109696;
    public static final int SENSOR_TYPE_AIRBAG_STATUS_PASSENGER = 2109952;
    public static final int SENSOR_TYPE_AQI_AMBIENT = 1049600;
    public static final int SENSOR_TYPE_AQI_INDOOR = 1049856;
    public static final int SENSOR_TYPE_AQI_LEVEL_AMBIENT = 2106112;
    public static final int SENSOR_TYPE_AQI_LEVEL_INDOOR = 2106368;
    public static final int SENSOR_TYPE_BRAKE_DEPTH = 1053440;
    public static final int SENSOR_TYPE_BRAKE_FLUID_LEVEL = 2098688;
    public static final int SENSOR_TYPE_CAR_MODE = 2102272;
    public static final int SENSOR_TYPE_CAR_SPEED = 1048832;
    public static final int SENSOR_TYPE_CAR_SPEED_ACCELERATION = 1054464;

    @VendorDefinition(author = "@ECARX", date = "2021-07-23", project = "SX11 AD")
    public static final int SENSOR_TYPE_CAR_SPEED_FROM_IPK = 1055232;
    public static final int SENSOR_TYPE_CO2_INDOOR = 1051904;
    public static final int SENSOR_TYPE_CO2_LEVEL_INDOOR = 2106624;
    public static final int SENSOR_TYPE_DAY_NIGHT = 2101248;
    public static final int SENSOR_TYPE_ENDURANCE_MILEAGE = 1050624;
    public static final int SENSOR_TYPE_ENDURANCE_MILEAGE_EV = 1054976;
    public static final int SENSOR_TYPE_ENDURANCE_MILEAGE_FUEL = 1054720;
    public static final int SENSOR_TYPE_ENGINE_COOLANT_LEVEL = 2098432;
    public static final int SENSOR_TYPE_ENGINE_COOLANT_TEMPERATURE = 1052416;
    public static final int SENSOR_TYPE_ENGINE_OIL_LEVEL = 2098176;
    public static final int SENSOR_TYPE_ENGINE_START_STOP_STATE = 2103040;
    public static final int SENSOR_TYPE_ENGINE_STATE = 2102784;

    @VendorDefinition(author = "@ECARX", date = "2022-02-24", project = "HX11")
    public static final int SENSOR_TYPE_EPTREADY = 3149568;
    public static final int SENSOR_TYPE_EV_BATTERY_LEVEL = 1051136;
    public static final int SENSOR_TYPE_EV_BATTERY_STATE = 2102528;

    @VendorDefinition(author = "@ECARX", date = "2021-7-13", project = "Smart")
    public static final int SENSOR_TYPE_EYE_BALL_TRACK_STATE = 3148800;
    public static final int SENSOR_TYPE_FUEL_LEVEL = 1050112;
    public static final int SENSOR_TYPE_GEAR = 2097664;
    public static final int SENSOR_TYPE_HANDBRAKE_STATE = 2097920;
    public static final int SENSOR_TYPE_IGNITION_STATE = 2097408;

    @VendorDefinition(author = "@ECARX", date = "2021-7-13", project = "HX11")
    public static final int SENSOR_TYPE_LANE_DEPARTURE = 3149056;
    public static final int SENSOR_TYPE_ODOMETER = 1050368;
    public static final int SENSOR_TYPE_PM25_AMBIENT = 1049088;
    public static final int SENSOR_TYPE_PM25_INDOOR = 1049344;
    public static final int SENSOR_TYPE_PM25_LEVEL_AMBIENT = 2105600;
    public static final int SENSOR_TYPE_PM25_LEVEL_INDOOR = 2105856;
    public static final int SENSOR_TYPE_PM25_STATE_AMBIENT = 2106880;
    public static final int SENSOR_TYPE_PM25_STATE_INDOOR = 2107136;
    public static final int SENSOR_TYPE_PM25_SWITCH = 1056768;
    public static final int SENSOR_TYPE_RAIN = 1052160;
    public static final int SENSOR_TYPE_RPM = 1050880;
    public static final int SENSOR_TYPE_SAFE_BELT_DRIVER = 2101760;
    public static final int SENSOR_TYPE_SAFE_BELT_PASSENGER = 2102016;
    public static final int SENSOR_TYPE_SAFE_BELT_ROW2_CENTER = 2103808;
    public static final int SENSOR_TYPE_SAFE_BELT_ROW2_LEFT = 2103296;
    public static final int SENSOR_TYPE_SAFE_BELT_ROW2_RIGHT = 2103552;
    public static final int SENSOR_TYPE_SEAT_OCCUPATION_STATUS_DRIVER = 2110208;
    public static final int SENSOR_TYPE_SEAT_OCCUPATION_STATUS_PASSENGER = 2110464;
    public static final int SENSOR_TYPE_SEAT_PRESSURE_DRIVER = 1053952;
    public static final int SENSOR_TYPE_SEAT_PRESSURE_PASSENGER = 1054208;
    public static final int SENSOR_TYPE_STEERING_WHEEL_ANGLE = 1052672;
    public static final int SENSOR_TYPE_STEERING_WHEEL_ANGLE_SPEED = 1052928;
    public static final int SENSOR_TYPE_TEMPERATURE_AMBIENT = 1051392;
    public static final int SENSOR_TYPE_TEMPERATURE_INDOOR = 1051648;

    @VendorDefinition(author = "@ECARX", date = "2021-6-26", project = "EX11")
    public static final int SENSOR_TYPE_TIREDNESS_DRIVING_STATE = 3148544;

    @VendorDefinition(author = "@ECARX", date = "20216-7-13", project = "HX11")
    public static final int SENSOR_TYPE_TOO_CLOSE_TO_FRONT_CAR = 3149312;
    public static final int SENSOR_TYPE_VEHICLE_WEIGHT = 1053184;

    @VendorDefinition(author = "@ECARX", date = "2021-4-12", project = "KX11/EX11")
    public static final int SENSOR_TYPE_VEH_MTN_STATE = 3148288;

    @VendorDefinition(author = "@ECARX", date = "2021-03-08", project = "E03", requirement = "")
    public static final int SENSOR_TYPE_WARN_ENGINE_COOLANT_SYSTEM_FAULT = 3148032;
    public static final int SENSOR_TYPE_WARN_ENGINE_COOLANT_TEMP_HIGH = 3146752;
    public static final int SENSOR_TYPE_WARN_ENGINE_OIL_PRESSURE = 3146496;

    @VendorDefinition(author = "@ECARX", date = "2021-03-08", project = "E03", requirement = "")
    public static final int SENSOR_TYPE_WARN_ENGINE_OIL_SYSTEM_FAULT = 3147776;
    public static final int SENSOR_TYPE_WARN_EV_BATTERY_LOW = 3146240;
    public static final int SENSOR_TYPE_WARN_FUEL_RED = 3145984;
    public static final int SENSOR_TYPE_WARN_STEERING_ASSISTANCE_FAULT = 3147520;
    public static final int SENSOR_TYPE_WARN_TRANSMISSION_SYSTEM_FAULT = 3147264;
    public static final int SENSOR_TYPE_WARN_TRANSMISSION_TEMP_HIGH = 3147008;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ContinuousSensor {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventSensor {
    }

    public interface ISensorListener {
        void onSensorEventChanged(int i, int i2);

        void onSensorSupportChanged(int i, FunctionStatus functionStatus);

        void onSensorValueChanged(int i, float f);
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface SensorRate {
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface SensorType {
    }

    ISensorGroup.IMountAngle getMountAngle();

    int getSensorEvent(int i);

    ISensorGroupValue getSensorGroupLatestValue(int i);

    float getSensorLatestValue(int i);

    FunctionStatus isSensorSupported(int i);

    boolean registerListener(ISensorListener iSensorListener, int i);

    boolean registerListener(ISensorListener iSensorListener, int i, int i2);

    void unregisterListener(ISensorListener iSensorListener);
}
