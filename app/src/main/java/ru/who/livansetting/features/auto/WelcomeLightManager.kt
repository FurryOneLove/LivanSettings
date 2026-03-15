package ru.who.livansetting.features.auto

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.ecarx.xui.adaptapi.car.vehicle.IVehicle
import ru.who.livansetting.services.MainService

/**
 * Менеджер для управления провожающим светом
 */
class WelcomeLightManager(private val context: Context) {
    
    companion object {
        private const val TAG = "WelcomeLightManager"
        private const val WELCOME_LIGHT_FUNCTION_ID = 537134848
        private const val PREFS_NAME = "welcome_light_prefs"
        private const val KEY_SAVED_VALUE = "saved_value"
        
        private val WELCOME_LIGHT_VALUES = arrayOf(
            IVehicle.HOME_SAFE_LIGHT_VALUE_OFF,
            IVehicle.HOME_SAFE_LIGHT_VALUE_30S,
            IVehicle.HOME_SAFE_LIGHT_VALUE_60S,
            IVehicle.HOME_SAFE_LIGHT_VALUE_90S
        )
    }
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private fun getCarService() = ru.who.livansetting.core.MainService.getInstance()?.getCarService()

    fun getWelcomeLightValue(): Int? = getCarService()?.getFunctionValue(WELCOME_LIGHT_FUNCTION_ID)
    
    fun setWelcomeLightValue(value: Int): Boolean {
        if (!WELCOME_LIGHT_VALUES.contains(value)) return false
        return getCarService()?.setFunctionValue(WELCOME_LIGHT_FUNCTION_ID, value) ?: false
    }
    
    fun toggleWelcomeLight() {
        val currentValue = getWelcomeLightValue() ?: return
        val currentIndex = WELCOME_LIGHT_VALUES.indexOf(currentValue)
        if (currentIndex == -1) {
            setWelcomeLightValue(IVehicle.HOME_SAFE_LIGHT_VALUE_OFF)
            return
        }
        val nextValue = WELCOME_LIGHT_VALUES[(currentIndex + 1) % WELCOME_LIGHT_VALUES.size]
        setWelcomeLightValue(nextValue)
    }
    
    fun isWelcomeLightEnabled(): Boolean? {
        val value = getWelcomeLightValue()
        return if (value != null) value != IVehicle.HOME_SAFE_LIGHT_VALUE_OFF else null
    }
    
    fun disableWelcomeLightWithSave(): Boolean {
        getWelcomeLightValue()?.takeIf { it != IVehicle.HOME_SAFE_LIGHT_VALUE_OFF }?.let {
            prefs.edit().putInt(KEY_SAVED_VALUE, it).apply()
        }
        return setWelcomeLightValue(IVehicle.HOME_SAFE_LIGHT_VALUE_OFF)
    }
    
    fun enableWelcomeLightWithRestore(): Boolean {
        val savedValue = prefs.getInt(KEY_SAVED_VALUE, IVehicle.HOME_SAFE_LIGHT_VALUE_30S)
        return setWelcomeLightValue(savedValue)
    }
}
