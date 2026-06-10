package ru.who.livansetting.features.navi

import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * Тестовый маршрут: проигрывает на приборке заранее заданную последовательность
 * манёвров, чтобы проверить вывод без реальной навигации.
 *
 * Использование:
 *   val test = NaviTestRoute(dimNaviManager)
 *   test.start()   // запустить проигрывание
 *   test.stop()    // прервать
 *
 * Каждый шаг публикуется через тот же путь, что и реальные данные
 * (DimNaviManager.publishTest -> updateNavigationInfo), поэтому проверяется
 * вся цепочка целиком.
 */
class NaviTestRoute(private val manager: DimNaviManager) {

    private val handler = Handler(Looper.getMainLooper())
    @Volatile
    private var running = false
    private var index = 0

    /** Один шаг тестового маршрута. */
    private data class Step(
        val maneuverDirection: String,
        val maneuverDistance: String,
        val nextRoad: String,
        val etaDistance: String,
        val etaTime: String,
        val holdMs: Long
    )

    private val steps = listOf(
        Step("context_ra_turn_right", "300 м", "Улица Ленина", "12 км", "18 мин", 3000),
        Step("context_ra_turn_right", "150 м", "Улица Ленина", "11 км", "17 мин", 3000),
        Step("context_ra_turn_left", "500 м", "проспект Мира", "10 км", "15 мин", 3000),
        Step("context_ra_in_circular_movement", "200 м", "Кольцо", "8 км", "12 мин", 3000),
        Step("context_ra_out_circular_movement", "80 м", "Шоссе Энтузиастов", "7 км", "10 мин", 3000),
        Step("context_ra_take_right", "1,2 км", "Объездная", "5 км", "7 мин", 3000),
        Step("context_ra_hard_turn_left", "400 м", "переулок Тупой", "2 км", "4 мин", 3000),
        Step("context_ra_finish", "120 м", "Точка назначения", "120 м", "1 мин", 4000)
    )

    fun start() {
        if (running) return
        running = true
        index = 0
        Log.i(TAG, "Test route started")
        playNext()
    }

    fun stop() {
        if (!running) return
        running = false
        handler.removeCallbacksAndMessages(null)
        manager.publishTestFinished()
        Log.i(TAG, "Test route stopped")
    }

    fun isRunning(): Boolean = running

    private fun playNext() {
        if (!running) return
        if (index >= steps.size) {
            // Маршрут пройден — снимаем с приборки.
            running = false
            manager.publishTestFinished()
            Log.i(TAG, "Test route finished")
            return
        }

        val step = steps[index]
        val state = NaviState(
            maneuverDirection = step.maneuverDirection,
            maneuverDistance = step.maneuverDistance,
            maneuverNextRoad = step.nextRoad,
            routeEtaDistance = step.etaDistance,
            routeEtaTime = step.etaTime,
            finished = false,
            lastEventTs = System.currentTimeMillis()
        )
        manager.publishTest(state)

        index++
        handler.postDelayed({ playNext() }, step.holdMs)
    }

    companion object {
        private const val TAG = "NaviTestRoute"
    }
}
