package android.car.hardware.property;

import android.car.CarManagerBase;
import android.car.hardware.CarPropertyValue;

/* loaded from: classes.dex */
public class CarPropertyManager implements CarManagerBase {

    public interface CarPropertyEventListener {
        void onChangeEvent(CarPropertyValue carPropertyValue);

        void onErrorEvent(int i, int i2);
    }

    @Override // android.car.CarManagerBase
    public void onCarDisconnected() {
        throw new RuntimeException("Stub!");
    }

    public boolean registerListener(CarPropertyEventListener carPropertyEventListener, int i, float f) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterListener(CarPropertyEventListener carPropertyEventListener) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterListener(CarPropertyEventListener carPropertyEventListener, int i) {
        throw new RuntimeException("Stub!");
    }

    public float getFloatProperty(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public int getIntProperty(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public boolean getBooleanProperty(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public int[] getIntArrayProperty(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public <E> CarPropertyValue<E> getProperty(Class<E> cls, int i, int i2) {
        throw new RuntimeException("Stub!");
    }
}
