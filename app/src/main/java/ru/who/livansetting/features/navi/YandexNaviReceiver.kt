package ru.who.livansetting.features.navi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * Принимает данные навигации от Яндекс.Навигатора.
 *
 * Яндекс шлёт серию broadcast-интентов с действиями
 *   ru.bluecat.yandexmapsnavigator.MANEUVER_INFO_UPDATED
 *   ru.bluecat.yandexmapsnavigator.ROUTE_INFO_UPDATED
 * В каждом интенте есть extra "type" (что обновилось) и "data" (значение строкой).
 *
 * Чтобы навигатор начал слать эти интенты, в самом Яндекс.Навигаторе должна быть
 * включена передача данных на бортовой экран (Настройки → ... → передача данных навигации).
 *
 * Ресивер аккумулирует поля в [NaviState] и уведомляет слушателя при каждом изменении.
 * Если событий нет дольше [INACTIVE_TIMEOUT_MS], маршрут считается снятым.
 */
class YandexNaviReceiver(private val listener: Listener) : BroadcastReceiver() {

    interface Listener {
        fun onNaviStateUpdated(state: NaviState)
        fun onRouteFinished()
    }

    private val state = NaviState()
    private val handler = Handler(Looper.getMainLooper())
    private val inactiveRunnable = Runnable {
        Log.d(TAG, "No events within timeout — route considered finished")
        state.finished = true
        listener.onRouteFinished()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        val type = intent.getStringExtra(EXTRA_TYPE)
        if (type == null) {
            Log.e(TAG, "type == null, ignored")
            return
        }

        state.lastEventTs = System.currentTimeMillis()
        scheduleInactivityCheck()

        if (type == TYPE_ROUTE_FINISHED) {
            state.finished = true
            listener.onRouteFinished()
            return
        }

        state.finished = false
        val data = intent.getStringExtra(EXTRA_DATA) ?: ""

        var changed = true
        when (type) {
            TYPE_ROAD_DIRECTION -> {
                if (state.maneuverDirection == data) changed = false
                else state.maneuverDirection = data
            }
            TYPE_ROAD_DISTANCE -> {
                if (state.maneuverDistance == data) changed = false
                else state.maneuverDistance = data
            }
            TYPE_ROAD_NAME -> {
                if (state.maneuverNextRoad == data) changed = false
                else state.maneuverNextRoad = data
            }
            TYPE_ETA_DISTANCE -> {
                if (state.routeEtaDistance == data) changed = false
                else state.routeEtaDistance = data
            }
            TYPE_ETA_TIME -> {
                if (state.routeEtaTime == data) changed = false
                else state.routeEtaTime = data
            }
            TYPE_ETA_ARRIVAL -> {
                if (state.routeEtaArrival == data) changed = false
                else state.routeEtaArrival = data
            }
            else -> {
                Log.d(TAG, "Unknown type=$type, ignored")
                changed = false
            }
        }

        if (changed) {
            listener.onNaviStateUpdated(state.copy())
        }
    }

    private fun scheduleInactivityCheck() {
        handler.removeCallbacks(inactiveRunnable)
        handler.postDelayed(inactiveRunnable, INACTIVE_TIMEOUT_MS)
    }

    companion object {
        private const val TAG = "YandexNaviReceiver"

        const val ACTION_MANEUVER_INFO_UPDATED =
            "ru.bluecat.yandexmapsnavigator.MANEUVER_INFO_UPDATED"
        const val ACTION_ROUTE_INFO_UPDATED =
            "ru.bluecat.yandexmapsnavigator.ROUTE_INFO_UPDATED"

        private const val EXTRA_TYPE = "type"
        private const val EXTRA_DATA = "data"

        // Значения extra "type"
        private const val TYPE_ROAD_DIRECTION = "road_direction"
        private const val TYPE_ROAD_DISTANCE = "road_distance"
        private const val TYPE_ROAD_NAME = "road_name"
        private const val TYPE_ETA_DISTANCE = "eta_distance"
        private const val TYPE_ETA_TIME = "eta_time"
        private const val TYPE_ETA_ARRIVAL = "eta_arrival"
        private const val TYPE_ROUTE_FINISHED = "route_finished"

        private const val INACTIVE_TIMEOUT_MS = 15_000L
    }
}
