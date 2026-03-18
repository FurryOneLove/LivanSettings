package ru.who.livansetting

import android.app.Application
import ru.who.livansetting.core.MainService

// Используется только в debug-сборке (debug source set).
// Запускает MainService при старте процесса, где BootReceiver не срабатывает
// (например, после adb install без перезагрузки).
// Реальные сервисы (I2CService, CarService, SensorService) обрабатывают недоступные API
// через try/catch, поэтому заглушки не нужны даже на эмуляторе.
class DebugApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Если сервис уже запущен (START_STICKY перезапуск системой), не вызываем повторно.
        if (MainService.getInstance() != null) return
        // Устанавливаем флаг до запуска сервиса — воспроизводит порядок штатного boot-флоу.
        MainService.setSystemBootComplete()
        MainService.startService(this)
    }
}
