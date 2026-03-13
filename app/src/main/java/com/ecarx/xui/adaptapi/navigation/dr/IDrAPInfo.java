package com.ecarx.xui.adaptapi.navigation.dr;

@Deprecated
/* loaded from: classes.dex */
public interface IDrAPInfo {

    public interface IAcc3d {
        int getAxis();

        int getInterval();

        long getTickTime();

        float getValuePitch();

        float getValueRoll();

        float getValueYaw();
    }

    public interface IGyro {
        int getAxis();

        int getInterval();

        float getTemperature();

        long getTickTime();

        float getValuePitch();

        float getValueRoll();

        float getValueYaw();
    }

    public interface IMountAngle {
        float getPitchMountAngle();

        float getRollMountAngle();

        float getYawMountAngle();

        boolean hasMountAngle();
    }

    public interface IPulse {
        int getInterval();

        long getTickTime();

        float getValue();
    }

    public interface IW4m {
        int getGearState();

        int getInterval();

        float getLatAcc();

        float getLonAcc();

        float getSteerAngle();

        long getTickTime();

        float getVFL();

        float getVFR();

        float getVRL();

        float getVRR();

        float getYawRate();
    }

    IAcc3d getAcc3d();

    IGyro getGyro();

    IMountAngle getMountAngle();

    IPulse getPulse();

    IW4m getW4m();
}
