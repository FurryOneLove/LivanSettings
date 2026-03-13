package com.ecarx.xui.adaptapi.car.sensor;

/* loaded from: classes.dex */
public interface ISensorGroupValue {

    public interface IAcc3dValue extends ISensorGroupValue {
        int getAxis();

        float getValuePitch();

        float getValueRoll();

        float getValueYaw();
    }

    public interface IGyroValue extends ISensorGroupValue {
        int getAxis();

        float getTemperature();

        float getValuePitch();

        float getValueRoll();

        float getValueYaw();
    }

    public interface ISpeedPulseValue extends ISensorGroupValue {
        float getSpeedValue();
    }

    public interface IW4mValue extends ISensorGroupValue {
        int getGearState();

        float getLatAcc();

        float getLonAcc();

        float getSteerAngle();

        float getVFLSpeed();

        float getVFRSpeed();

        float getVRLSpeed();

        float getVRRSpeed();

        float getYawRate();
    }

    int getInterval();

    int getSensorGroupType();

    long getTickTime();
}
