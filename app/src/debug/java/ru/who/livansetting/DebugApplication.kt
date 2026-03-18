package ru.who.livansetting

import android.app.Application
import android.util.Log
import ru.who.livansetting.core.MainService
import ru.who.livansetting.core.StubCarService
import ru.who.livansetting.core.StubI2CService
import ru.who.livansetting.core.StubSensorService
import ru.who.livansetting.utils.EmulatorDetector

// Используется только в debug-сборке (debug source set).
// Запускает MainService при старте процесса на эмуляторе,
// где BootReceiver не срабатывает после adb install.
class DebugApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (EmulatorDetector.isEmulator) {
            Log.i("DebugApplication", "Running in emulator mode - eCarX stubs enabled")
            MainService.setDebugServiceOverrides(
                StubCarService(),
                StubI2CService(),
                StubSensorService()
            )
        }

        // Если сервис уже запущен (START_STICKY перезапуск системой), не вызываем повторно.
        if (MainService.getInstance() != null) return
        // Устанавливаем флаг до запуска сервиса — воспроизводит порядок штатного boot-флоу.
        MainService.setSystemBootComplete()
        MainService.startService(this)
    }
}
