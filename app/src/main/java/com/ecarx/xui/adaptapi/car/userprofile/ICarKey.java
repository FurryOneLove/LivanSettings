package com.ecarx.xui.adaptapi.car.userprofile;

/* loaded from: classes.dex */
public interface ICarKey {

    public interface ICarKeyObserver {
        void multipleKeyFound(boolean z);

        void onKeyReadResult(int i);

        void timeout();
    }

    void cancelDiscovery();

    void readRealKey();

    boolean registerCarKeyObserver(ICarKeyObserver iCarKeyObserver);

    void startDiscovery();

    void unbindCarKey(int i);

    boolean unregisterCarKeyObserver(ICarKeyObserver iCarKeyObserver);
}
