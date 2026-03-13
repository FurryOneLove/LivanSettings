package ru.who.livansetting.utils

import android.util.Log

object BuildPropUtils {
    private const val TAG = "BuildPropUtils"
    
    /**
     * Получает значение свойства из build.prop
     * @param key ключ свойства
     * @param defaultValue значение по умолчанию
     * @return значение свойства или defaultValue если не найдено
     */
    fun getprop(key: String, defaultValue: String? = null): String? {
        return try {
            val cls = Class.forName("android.os.SystemProperties")
            val method = cls.getMethod("get", String::class.java, String::class.java)
            val result = method.invoke(cls, key, null) as String?
            
            if (result != null && result.isNotEmpty()) {
                result
            } else {
                defaultValue
            }
        } catch (e: Exception) {
            Log.e(TAG, "getprop exception: $e")
            defaultValue
        }
    }
    
    /**
     * Проверяет, содержит ли ro.build.flavor указанные символы
     * @param flavorSubstring подстрока для поиска в ro.build.flavor
     * @return true если подстрока найдена, false иначе
     */
    fun isFlavorContains(flavorSubstring: String): Boolean {
        val flavor = getprop("ro.build.flavor", "")
        return flavor?.contains(flavorSubstring) == true
    }
    
    /**
     * Проверяет, нужно ли скрыть действие "Включить/выключить ДХО"
     * @return true если нужно скрыть (если ro.build.flavor содержит "601")
     */
    fun shouldHideDrlAction(): Boolean {
        return isFlavorContains("601")
    }
    
    /**
     * Проверяет, нужно ли скрыть режим "Адаптив" из выбора режима вождения
     * @return true если нужно скрыть (если ro.build.flavor содержит "602")
     */
    fun shouldHideAdaptiveMode(): Boolean {
        return isFlavorContains("602")
    }
}






