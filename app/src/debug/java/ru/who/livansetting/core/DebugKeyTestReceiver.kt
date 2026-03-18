package ru.who.livansetting.core

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import ru.who.livansetting.features.keys.IICKeyCodes

// This class exists only in the debug source set — it is never compiled into release builds.
class DebugKeyTestReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "DebugKeyTestReceiver"
        const val ACTION_SIMULATE_KEY = "ru.who.livansetting.action.DEBUG_SIMULATE_KEY"
        const val EXTRA_KEY_CODE = "keyCode"
        const val EXTRA_IS_LONG_PRESS = "isLongPress"

        private const val SHORT_PRESS_RELEASE_DELAY_MS = 100L
        private const val LONG_PRESS_RELEASE_DELAY_MS = 1100L

        // Mirrors SimpleKeyHandler.SHORT_PRESS_ONLY_KEYS
        private val SHORT_PRESS_ONLY_KEYS = setOf(
            IICKeyCodes.KEY_CODE_IIC_VOLUME_UP,
            IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN,
            IICKeyCodes.KEY_CODE_IIC_MUTE,
            IICKeyCodes.KEY_CODE_IIC_POWER
        )
    }

    @SuppressLint("HandlerLeak") // Safe: no reference to outer class; keyHandler is a captured local val
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_SIMULATE_KEY) return

        if (!intent.hasExtra(EXTRA_KEY_CODE)) {
            Log.w(TAG, "Missing extra: $EXTRA_KEY_CODE")
            return
        }

        val keyCode = intent.getIntExtra(EXTRA_KEY_CODE, -1)
        if (keyCode <= 0) {
            Log.w(TAG, "Invalid keyCode: $keyCode")
            return
        }

        val isLongPress = intent.getBooleanExtra(EXTRA_IS_LONG_PRESS, false)

        val keyHandler = MainService.getInstance()?.getSimpleKeyHandler()
        if (keyHandler == null) {
            Log.e(TAG, "MainService not running or SimpleKeyHandler unavailable")
            return
        }

        val effectiveIsLongPress = if (isLongPress && keyCode in SHORT_PRESS_ONLY_KEYS) {
            Log.w(TAG, "keyCode=$keyCode is SHORT_PRESS_ONLY — ignoring isLongPress=true, simulating short press")
            false
        } else {
            isLongPress
        }

        val releaseDelayMs = if (effectiveIsLongPress) LONG_PRESS_RELEASE_DELAY_MS else SHORT_PRESS_RELEASE_DELAY_MS
        Log.d(TAG, "Simulating key: keyCode=$keyCode isLongPress=$effectiveIsLongPress releaseDelay=${releaseDelayMs}ms")

        keyHandler.handleKeyEvent(keyCode, 1)
        // NOTE: postDelayed relies on MainService foreground priority keeping the process alive
        // for the full releaseDelayMs window. Acceptable for a debug-only test helper.
        Handler(Looper.getMainLooper()).postDelayed({
            keyHandler.handleKeyEvent(keyCode, 0)
        }, releaseDelayMs)
    }
}
