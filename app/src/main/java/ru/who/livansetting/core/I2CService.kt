package ru.who.livansetting.core

import android.content.Context
import android.util.Log
import com.ecarx.xui.adaptapi.policy.II2cCommunication
import com.ecarx.xui.adaptapi.policy.I2cCallback
import com.ecarx.xui.adaptapi.policy.I2cReadyCallback
import com.ecarx.xui.adaptapi.policy.Policy
import ru.who.livansetting.features.keys.IICKeyCodes
import ru.who.livansetting.utils.BuildPropUtils

/**
 * Сервис для работы с I2C интерфейсом
 */
class I2CService(private val context: Context) : II2CService {

    companion object {
        private const val TAG = "I2CService"
    }

    private var policy: Policy? = null
    private var i2cCommunication: II2cCommunication? = null
    private var keyEventHandler: ((Int, Boolean) -> Unit)? = null
    private var isInitialized = false
    @Volatile private var isListenerRegistered = false

    override fun initialize() {
        try {
            policy = Policy.create(context) ?: return
            i2cCommunication = policy?.i2cCommunication ?: return

            i2cCommunication!!.setOnReadyCallback(object : I2cReadyCallback {
                override fun onReady() {
                    onI2CReady()
                }
            })

            if (i2cCommunication!!.isReady()) {
                onI2CReady()
            }
        } catch (e: Exception) {
            Log.e(TAG, "I2C init error", e)
        }
    }

    private fun onI2CReady() {
        if (isListenerRegistered) return

        val success = i2cCommunication?.registerI2cListener(object : I2cCallback {
            override fun onReceiveI2cData(data: IntArray) {
                Log.d(TAG, "I2C data received: ${data.joinToString()}")
                if (data.isNotEmpty() && data[0] == 16) {
                    processKeyData(data)
                }
            }
        }) ?: false
        if (success) {
            isListenerRegistered = true
            isInitialized = true
        }
    }

    private fun processKeyData(data: IntArray) {
        val flavor601 = BuildPropUtils.isFlavorContains("601")

        // Volume Up
        if (!flavor601 && data.size > IICKeyCodes.IIC_DATA_INDEX_VOLUME_UP) {
            if ((data[IICKeyCodes.IIC_DATA_INDEX_VOLUME_UP] and IICKeyCodes.IIC_PRESS_MASK) != 0) {
                Log.d(TAG, "I2C key event: keyCode=${IICKeyCodes.KEY_CODE_IIC_VOLUME_UP} isRelease=false")
                handleKey(IICKeyCodes.KEY_CODE_IIC_VOLUME_UP, false)
            }
            if ((data[IICKeyCodes.IIC_DATA_INDEX_VOLUME_UP] and IICKeyCodes.IIC_RELEASE_MASK) != 0) {
                Log.d(TAG, "I2C key event: keyCode=${IICKeyCodes.KEY_CODE_IIC_VOLUME_UP} isRelease=true")
                handleKey(IICKeyCodes.KEY_CODE_IIC_VOLUME_UP, true)
            }
        }

        // Mute
        if (data.size > IICKeyCodes.IIC_DATA_INDEX_MUTE) {
            if ((data[IICKeyCodes.IIC_DATA_INDEX_MUTE] and IICKeyCodes.IIC_PRESS_MASK) != 0) {
                Log.d(TAG, "I2C key event: keyCode=${IICKeyCodes.KEY_CODE_IIC_MUTE} isRelease=false")
                handleKey(IICKeyCodes.KEY_CODE_IIC_MUTE, false)
            }
            if ((data[IICKeyCodes.IIC_DATA_INDEX_MUTE] and IICKeyCodes.IIC_RELEASE_MASK) != 0) {
                Log.d(TAG, "I2C key event: keyCode=${IICKeyCodes.KEY_CODE_IIC_MUTE} isRelease=true")
                handleKey(IICKeyCodes.KEY_CODE_IIC_MUTE, true)
            }
        }

        // Volume Down
        if (!flavor601 && data.size > IICKeyCodes.IIC_DATA_INDEX_VOLUME_DOWN) {
            if ((data[IICKeyCodes.IIC_DATA_INDEX_VOLUME_DOWN] and IICKeyCodes.IIC_PRESS_MASK) != 0) {
                Log.d(TAG, "I2C key event: keyCode=${IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN} isRelease=false")
                handleKey(IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN, false)
            }
            if ((data[IICKeyCodes.IIC_DATA_INDEX_VOLUME_DOWN] and IICKeyCodes.IIC_RELEASE_MASK) != 0) {
                Log.d(TAG, "I2C key event: keyCode=${IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN} isRelease=true")
                handleKey(IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN, true)
            }
        }

        // Power
        if (data.size > IICKeyCodes.IIC_DATA_INDEX_POWER) {
            if ((data[IICKeyCodes.IIC_DATA_INDEX_POWER] and IICKeyCodes.IIC_PRESS_MASK) != 0) {
                Log.d(TAG, "I2C key event: keyCode=${IICKeyCodes.KEY_CODE_IIC_POWER} isRelease=false")
                handleKey(IICKeyCodes.KEY_CODE_IIC_POWER, false)
            }
            if ((data[IICKeyCodes.IIC_DATA_INDEX_POWER] and IICKeyCodes.IIC_RELEASE_MASK) != 0) {
                Log.d(TAG, "I2C key event: keyCode=${IICKeyCodes.KEY_CODE_IIC_POWER} isRelease=true")
                handleKey(IICKeyCodes.KEY_CODE_IIC_POWER, true)
            }
        }
    }

    private fun handleKey(keyCode: Int, isRelease: Boolean) {
        keyEventHandler?.invoke(keyCode, isRelease)
    }

    override fun release() {
        isInitialized = false
        isListenerRegistered = false
        i2cCommunication = null
        policy = null
    }

    override fun setKeyEventHandler(handler: (Int, Boolean) -> Unit) {
        keyEventHandler = handler
    }

    override fun isInitialized(): Boolean = isInitialized
}
