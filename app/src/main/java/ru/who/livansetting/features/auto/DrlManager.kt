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

    fun toggleDrl() {
        val carService = MainService.getInstance()?.getCarService() ?: return
        val currentStatus = carService.getFunctionValue(DRL_FUNCTION_ID) ?: DRL_STATE_ON
        val nextStatus = if (currentStatus == DRL_STATE_ON) DRL_STATE_OFF else DRL_STATE_ON
        carService.setFunctionValue(DRL_FUNCTION_ID, nextStatus)
        Log.d(TAG, "DRL toggled to: $nextStatus")
    }
}
