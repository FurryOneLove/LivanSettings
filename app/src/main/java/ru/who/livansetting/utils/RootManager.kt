package ru.who.livansetting.utils

import android.content.Context
import android.util.Log
import android.view.KeyEvent

/**
 * Менеджер для работы с root доступом
 * Обеспечивает отправку KeyEvent через root команды с кэшированием и fallback механизмами
 */
class RootManager private constructor(private val context: Context) {
    
    companion object {
        private const val TAG = "RootManager"
        private const val ROOT_CHECK_TIMEOUT = 30000L // 30 секунд
        
        @Volatile
        private var instance: RootManager? = null
        
        /**
         * Получает экземпляр RootManager
         * @param context контекст приложения
         * @return экземпляр RootManager
         */
        fun getInstance(context: Context): RootManager {
            return instance ?: synchronized(this) {
                instance ?: RootManager(context).also { instance = it }
            }
        }
        
        /**
         * Получает существующий экземпляр RootManager
         * @return экземпляр RootManager или исключение если не инициализирован
         */
        fun getInstance(): RootManager {
            return instance ?: throw RuntimeException("RootManager.instance == null, error on call getInstance()")
        }
    }
    
    // Кэш для root доступа
    private var rootAccessCached: Boolean? = null
    private var lastRootCheck = 0L
    
    /**
     * Проверяет доступность root доступа с кэшированием
     * @return true если root доступен
     */
    fun isRootAccessAvailable(): Boolean {
        val currentTime = System.currentTimeMillis()
        
        // Проверяем кэш
        if (rootAccessCached != null && currentTime - lastRootCheck < ROOT_CHECK_TIMEOUT) {
            Log.d(TAG, "Using cached root access status: $rootAccessCached")
            return rootAccessCached!!
        }
        
        return try {
            Log.d(TAG, "Checking root access availability...")
            val process = Runtime.getRuntime().exec("su -c 'echo test'")
            val exitCode = process.waitFor()
            val success = exitCode == 0
            
            // Обновляем кэш
            rootAccessCached = success
            lastRootCheck = currentTime
            
            if (success) {
                Log.d(TAG, "Root access is available")
            } else {
                Log.w(TAG, "Root access is not available (exit code: $exitCode)")
            }
            
            success
        } catch (e: Exception) {
            Log.e(TAG, "Error checking root access", e)
            // Обновляем кэш с отрицательным результатом
            rootAccessCached = false
            lastRootCheck = currentTime
            false
        }
    }
    
    /**
     * Отправляет KeyEvent через root доступ
     * @param keyCode код клавиши
     * @return true если успешно отправлено
     */
    fun sendKeyEventViaRoot(keyCode: Int): Boolean {
        return try {
            Log.d(TAG, "Attempting to send KeyEvent via root: $keyCode")
            
            // Создаем KeyEvent для нажатия
            val downTime = System.currentTimeMillis()
            val eventTime = System.currentTimeMillis()
            
            val keyDownEvent = KeyEvent(downTime, eventTime, KeyEvent.ACTION_DOWN, keyCode, 0)
            val keyUpEvent = KeyEvent(downTime, eventTime + 50, KeyEvent.ACTION_UP, keyCode, 0)
            
            // Отправляем через root доступ
            val result = sendKeyEventViaRootCommand(keyCode)
            
            if (result) {
                Log.d(TAG, "KeyEvent sent successfully via root command")
                true
            } else {
                Log.w(TAG, "Failed to send KeyEvent via root command")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending KeyEvent via root", e)
            false
        }
    }
    
    /**
     * Отправляет KeyEvent через root команды
     * @param keyCode код клавиши
     * @return true если успешно отправлено
     */
    private fun sendKeyEventViaRootCommand(keyCode: Int): Boolean {
        return try {
            Log.d(TAG, "Executing root command for keyCode: $keyCode")
            
            // Используем более надежный способ выполнения root команд
            val command = arrayOf("su", "-c", "input keyevent $keyCode")
            val process = Runtime.getRuntime().exec(command)
            
            // Читаем вывод для отладки
            val inputStream = process.inputStream
            val errorStream = process.errorStream
            val inputReader = java.io.BufferedReader(java.io.InputStreamReader(inputStream))
            val errorReader = java.io.BufferedReader(java.io.InputStreamReader(errorStream))
            
            val inputLines = inputReader.readLines()
            val errorLines = errorReader.readLines()
            
            inputReader.close()
            errorReader.close()
            inputStream.close()
            errorStream.close()
            
            val exitCode = process.waitFor()
            val success = exitCode == 0
            
            if (success) {
                Log.d(TAG, "Root command executed successfully for keyCode: $keyCode")
                if (inputLines.isNotEmpty()) {
                    Log.d(TAG, "Command output: ${inputLines.joinToString()}")
                }
            } else {
                Log.w(TAG, "Root command failed with exit code: $exitCode")
                if (errorLines.isNotEmpty()) {
                    Log.w(TAG, "Command error: ${errorLines.joinToString()}")
                }
            }
            
            success
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException: Root access denied for KeyEvent", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Error executing root command for KeyEvent", e)
            false
        }
    }
    
    /**
     * Выполняет произвольную root команду
     * @param command команда для выполнения
     * @return результат выполнения команды
     */
    fun executeRootCommand(command: String): RootCommandResult {
        return try {
            Log.d(TAG, "Executing root command: $command")
            
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            
            // Читаем вывод
            val inputStream = process.inputStream
            val errorStream = process.errorStream
            val inputReader = java.io.BufferedReader(java.io.InputStreamReader(inputStream))
            val errorReader = java.io.BufferedReader(java.io.InputStreamReader(errorStream))
            
            val inputLines = inputReader.readLines()
            val errorLines = errorReader.readLines()
            
            inputReader.close()
            errorReader.close()
            inputStream.close()
            errorStream.close()
            
            val exitCode = process.waitFor()
            val success = exitCode == 0
            
            RootCommandResult(
                success = success,
                exitCode = exitCode,
                output = inputLines,
                error = errorLines
            )
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException: Root access denied for command: $command", e)
            RootCommandResult(
                success = false,
                exitCode = -1,
                output = emptyList(),
                error = listOf("SecurityException: ${e.message}")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error executing root command: $command", e)
            RootCommandResult(
                success = false,
                exitCode = -1,
                output = emptyList(),
                error = listOf("Exception: ${e.message}")
            )
        }
    }
    
    /**
     * Очищает кэш root доступа
     */
    fun clearRootAccessCache() {
        rootAccessCached = null
        lastRootCheck = 0L
        Log.d(TAG, "Root access cache cleared")
    }
    
    /**
     * Получает информацию о кэше root доступа
     * @return Map с информацией о кэше
     */
    fun getRootCacheInfo(): Map<String, Any> {
        val currentTime = System.currentTimeMillis()
        return mapOf(
            "cached" to (rootAccessCached != null),
            "value" to (rootAccessCached ?: false),
            "last_check" to lastRootCheck,
            "cache_age_ms" to (currentTime - lastRootCheck),
            "is_cache_valid" to (currentTime - lastRootCheck < ROOT_CHECK_TIMEOUT)
        )
    }
    
    /**
     * Получает расширенную статистику root доступа
     * @return Map с расширенной информацией
     */
    fun getRootStats(): Map<String, Any> {
        val isAvailable = isRootAccessAvailable()
        val cacheInfo = getRootCacheInfo()
        
        return mapOf(
            "root_available" to isAvailable,
            "cache_info" to cacheInfo,
            "recommended_usage" to if (isAvailable) "Use root commands for better reliability" else "Use fallback methods"
        )
    }
}

/**
 * Результат выполнения root команды
 */
data class RootCommandResult(
    val success: Boolean,
    val exitCode: Int,
    val output: List<String>,
    val error: List<String>
) {
    /**
     * Получает объединенный вывод команды
     */
    fun getCombinedOutput(): String {
        return if (output.isNotEmpty()) {
            output.joinToString("\n")
        } else {
            error.joinToString("\n")
        }
    }
    
    /**
     * Проверяет, была ли команда выполнена успешно
     */
    fun isSuccessful(): Boolean = success && exitCode == 0
}
