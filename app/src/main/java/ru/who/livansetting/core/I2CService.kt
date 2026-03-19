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
    @Volatile private var previousData: IntArray? = null

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
        synchronized(this) {
            val prev = previousData
            val flavor601 = BuildPropUtils.isFlavorContains("601")

            fun checkButton(index: Int, keyCode: Int, skip: Boolean = false) {
                if (skip) return
                if (data.size <= index) return
                val cur = data[index]
                val pre = if (prev != null && prev.size > index) prev[index] else 0
                if (cur != 0 && pre == 0) {
                    Log.d(TAG, "I2C key event: keyCode=$keyCode isRelease=false")
                    handleKey(keyCode, false)
                } else if (cur == 0 && pre != 0) {
                    Log.d(TAG, "I2C key event: keyCode=$keyCode isRelease=true")
                    handleKey(keyCode, true)
                }
            }

            checkButton(IICKeyCodes.IIC_DATA_INDEX_VOLUME_UP, IICKeyCodes.KEY_CODE_IIC_VOLUME_UP, skip = flavor601)
            checkButton(IICKeyCodes.IIC_DATA_INDEX_MUTE, IICKeyCodes.KEY_CODE_IIC_MUTE)
            checkButton(IICKeyCodes.IIC_DATA_INDEX_VOLUME_DOWN, IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN, skip = flavor601)
            checkButton(IICKeyCodes.IIC_DATA_INDEX_POWER, IICKeyCodes.KEY_CODE_IIC_POWER)

            previousData = data.copyOf()
        }
    }

    private fun handleKey(keyCode: Int, isRelease: Boolean) {
        keyEventHandler?.invoke(keyCode, isRelease)
    }

    override fun release() {
        previousData = null
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
