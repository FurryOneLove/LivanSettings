package ru.who.livansetting.features.auto

import android.content.Context
import android.util.Log
import ru.who.livansetting.core.MainService

/**
 * Менеджер для управления дневными ходовыми огнями (DRL)
 */
class DrlManager(private val context: Context) {
    companion object {
        private const val TAG = "DrlManager"
        private const val DRL_FUNCTION_ID = 537135104
        private const val DRL_STATE_OFF = 0
        private const val DRL_STATE_ON = 1
    }

    private var lastKnownDrlState: Int? = null

    @Synchronized
    fun toggleDrl() {
        val carService = MainService.getInstance()?.getCarService() ?: run {
            Log.w(TAG, "DRL toggle skipped: carService not available")
            return
        }
        val liveState = carService.getFunctionValue(DRL_FUNCTION_ID)
        val currentStatus = liveState ?: (lastKnownDrlState ?: DRL_STATE_OFF)
        val nextStatus = if (currentStatus == DRL_STATE_ON) DRL_STATE_OFF else DRL_STATE_ON
        if (carService.setFunctionValue(DRL_FUNCTION_ID, nextStatus)) {
            lastKnownDrlState = nextStatus
            Log.d(TAG, "DRL toggled to: $nextStatus (liveState=$liveState)")
        } else {
            Log.w(TAG, "DRL setFunctionValue failed (liveState=$liveState)")
        }
    }
}
