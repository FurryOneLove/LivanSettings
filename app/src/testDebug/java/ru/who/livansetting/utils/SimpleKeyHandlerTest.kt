package ru.who.livansetting.utils

import android.content.Context
import android.util.Log
import com.ecarx.xui.adaptapi.input.KeyCode
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class SimpleKeyHandlerTest {

    private val context = mockk<Context>(relaxed = true)
    private val keyActionExecutor = mockk<KeyActionExecutor>(relaxed = true)
    private lateinit var keyHandler: SimpleKeyHandler

    @Before
    fun setUp() {
        // Мокаем статический логгер Android
        mockkStatic(Log::class)
        every { Log.d(any<String>(), any<String>()) } returns 0
        every { Log.w(any<String>(), any<String>()) } returns 0
        every { Log.e(any<String>(), any<String>(), any<Throwable>()) } returns 0

        // Передаем мок исполнителя в обработчик (Dependency Injection)
        keyHandler = SimpleKeyHandler(context, keyActionExecutor)
    }

    @After
    fun tearDown() {
        keyHandler.shutdown()
        unmockkAll()
    }

    @Test
    fun `test short press triggers correct action`() {
        val keyCode = KeyCode.KEYCODE_R_MEDIA_NEXT
        
        // Эмулируем нажатие (action = 1)
        keyHandler.handleKeyEvent(keyCode, 1)
        
        // Эмулируем немедленное отпускание (action = 0)
        keyHandler.handleKeyEvent(keyCode, 0)
        
        // Проверяем, что был вызван метод с параметром isLongPress = false
        verify(exactly = 1) { 
            keyActionExecutor.handleKeyPressWithRemapping(keyCode, false)
        }
        
        // Проверяем, что длинное нажатие НЕ было зафиксировано
        verify(exactly = 0) { 
            keyActionExecutor.handleKeyPressWithRemapping(keyCode, true)
        }
    }

    @Test
    fun `test long press triggers correct action`() {
        val keyCode = KeyCode.KEYCODE_R_MEDIA_NEXT
        
        // Эмулируем нажатие
        keyHandler.handleKeyEvent(keyCode, 1)
        
        // Ждем чуть больше секунды (LONG_PRESS_DURATION = 1000ms)
        Thread.sleep(1200)
        
        // Проверяем, что действие длинного нажатия сработало по таймеру
        verify(timeout = 1500) { 
            keyActionExecutor.handleKeyPressWithRemapping(keyCode, true)
        }
        
        // Эмулируем отпускание
        keyHandler.handleKeyEvent(keyCode, 0)
        
        // После отпускания повторный вызов (как для короткого) не должен произойти
        verify(exactly = 0) { 
            keyActionExecutor.handleKeyPressWithRemapping(keyCode, false)
        }
    }

    @Test
    fun `test debounce ignores fast repeated presses`() {
        val keyCode = KeyCode.KEYCODE_R_HOME
        
        // Первое нажатие
        keyHandler.handleKeyEvent(keyCode, 1)
        keyHandler.handleKeyEvent(keyCode, 0)
        
        // Второе нажатие через 10мс (DEBOUNCE_DURATION = 50ms)
        Thread.sleep(10)
        keyHandler.handleKeyEvent(keyCode, 1)
        keyHandler.handleKeyEvent(keyCode, 0)
        
        // Должно обработаться только одно нажатие из-за debounce
        verify(exactly = 1) { 
            keyActionExecutor.handleKeyPressWithRemapping(keyCode, any<Boolean>())
        }
    }

    @Test
    fun `test volume up release triggers stop operation`() {
        val keyCode = KeyCode.KEYCODE_R_VOLUME_UP
        
        // Нажимаем и отпускаем клавишу громкости
        keyHandler.handleKeyEvent(keyCode, 1)
        keyHandler.handleKeyEvent(keyCode, 0)
        
        // Должен быть вызван метод отпускания для остановки изменения громкости
        verify { keyActionExecutor.handleVolumeUpRelease() }
    }
}
