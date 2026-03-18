package ru.who.livansetting.core

import android.util.Log

class StubI2CService : II2CService {

    companion object {
        private const val TAG = "StubI2CService"
    }

    override fun initialize() {
        Log.d(TAG, "initialize called (emulator mode)")
    }

    override fun setKeyEventHandler(handler: (Int, Boolean) -> Unit) {
        Log.d(TAG, "setKeyEventHandler called (emulator mode)")
    }

    override fun release() {
        Log.d(TAG, "release called (emulator mode)")
    }

    override fun isInitialized(): Boolean {
        Log.d(TAG, "isInitialized called (emulator mode)")
        return false
    }
}
