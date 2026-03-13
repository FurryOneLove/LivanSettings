package com.ecarx.xui.adaptapi.car.impl;

import android.content.Context;
import com.ecarx.xui.adaptapi.car.ICar;
import com.ecarx.xui.adaptapi.car.base.ICarFunction;
import com.ecarx.xui.adaptapi.car.base.ICarInfo;
import com.ecarx.xui.adaptapi.car.diagnostics.IDiagnostics;
import com.ecarx.xui.adaptapi.car.hev.IHev;
import com.ecarx.xui.adaptapi.car.impl.vehicle.VehicleSignalManager;
import com.ecarx.xui.adaptapi.car.sensor.ISensor;
import com.ecarx.xui.adaptapi.car.userprofile.ICarKey;
import com.ecarx.xui.adaptapi.car.userprofile.IUserProfile;

/* loaded from: classes.dex */
public class CarImpl extends VehicleSignalManager implements ICar {
    public CarImpl(Context context) {
        throw new RuntimeException("Stub!");
    }

    @Override // com.ecarx.xui.adaptapi.car.ICar
    public ICarInfo getCarInfoManager() {
        throw new RuntimeException("Stub!");
    }

    @Override // com.ecarx.xui.adaptapi.car.ICar
    public ICarKey getCarKeyManager() {
        throw new RuntimeException("Stub!");
    }

    @Override // com.ecarx.xui.adaptapi.car.ICar
    public IDiagnostics getDiagnosticManager() {
        throw new RuntimeException("Stub!");
    }

    @Override // com.ecarx.xui.adaptapi.car.ICar
    public IHev getHevManager() {
        throw new RuntimeException("Stub!");
    }

    @Override // com.ecarx.xui.adaptapi.car.ICar
    public ICarFunction getICarFunction() {
        throw new RuntimeException("Stub!");
    }

    @Override // com.ecarx.xui.adaptapi.car.ICar
    public ISensor getSensorManager() {
        throw new RuntimeException("Stub!");
    }

    @Override // com.ecarx.xui.adaptapi.car.ICar
    public IUserProfile getUserProfileManager() {
        throw new RuntimeException("Stub!");
    }

    public byte[] getBytesByPropID(int i) {
        throw new RuntimeException("Stub!");
    }

    public byte[] getBytesByPropID(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public int getIntByPropID(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getIntByPropID(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public boolean sendIntToMCUByPropID(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public boolean sendIntToMCUByPropID(int i, int i2, int i3) {
        throw new RuntimeException("Stub!");
    }

    public boolean sendBytesToMcuByPropID(int i, byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public boolean sendBytesToMcuByPropID(int i, int i2, byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public int getACCState() {
        throw new RuntimeException("Stub!");
    }

    public int getEngineState() {
        throw new RuntimeException("Stub!");
    }

    public int getVehicleGearPosition() {
        throw new RuntimeException("Stub!");
    }

    public float getVehicleInstantaneousSpeed() {
        throw new RuntimeException("Stub!");
    }

    public int getVehicleTotalMileage() {
        throw new RuntimeException("Stub!");
    }

    public int getVehicleAverageFuel() {
        throw new RuntimeException("Stub!");
    }

    public float getVehicleDrivingRankRANKFuel() {
        throw new RuntimeException("Stub!");
    }

    public int getIPKLanguage() {
        throw new RuntimeException("Stub!");
    }

    public int getIPKCorrectLativeState() {
        throw new RuntimeException("Stub!");
    }

    public int getIPKSkinMode() {
        throw new RuntimeException("Stub!");
    }

    public int getLeftDirectionLightState() {
        throw new RuntimeException("Stub!");
    }

    public int getRightDirectionLightState() {
        throw new RuntimeException("Stub!");
    }
}
