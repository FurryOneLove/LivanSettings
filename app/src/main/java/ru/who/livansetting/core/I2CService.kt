package ru.who.livansetting.core

import android.content.Context
import android.util.Log
import ru.who.livansetting.features.keys.IICKeyCodes
import ru.who.livansetting.utils.BuildPropUtils

class I2CService(private val context: Context) : II2CService {

    companion object {
        private const val TAG = "I2CService"
    }

    private var policy: Any? = null
    private var i2cCommunication: Any? = null
    private var keyEventHandler: ((Int, Boolean) -> Unit)? = null
    private val flavor601 = BuildPropUtils.isFlavorContains("601")
    private val flavor602 = BuildPropUtils.isFlavorContains("602")

    @Volatile private var isInitialized = false
    @Volatile private var isListenerRegistered = false
    @Volatile private var previousData: IntArray? = null

    override fun initialize() {
        try {
            val policyClass = Class.forName("com.ecarx.xui.adaptapi.policy.Policy")
            val createMethod = policyClass.getMethod("create", Context::class.java)
            val policyInstance = createMethod.invoke(null, context) ?: return
            policy = policyInstance

            val getI2c = policyClass.getMethod("getI2cCommunication")
            val i2cInstance = getI2c.invoke(policyInstance) ?: return
            i2cCommunication = i2cInstance

            val i2cClass = Class.forName("com.ecarx.xui.adaptapi.policy.II2cCommunication")
            val readyCallbackClass = Class.forName("com.ecarx.xui.adaptapi.policy.I2cReadyCallback")
            val callbackClass = Class.forName("com.ecarx.xui.adaptapi.policy.I2cCallback")

            val setOnReadyCallback = i2cClass.getMethod("setOnReadyCallback", readyCallbackClass)
            val readyCallback = java.lang.reflect.Proxy.newProxyInstance(
                readyCallbackClass.classLoader,
                arrayOf(readyCallbackClass)
            ) { _, method, _ ->
                if (method.name == "onReady") {
                    onI2CReady(i2cInstance, i2cClass, callbackClass)
                }
                null
            }
            setOnReadyCallback.invoke(i2cInstance, readyCallback)

            val isReadyMethod = i2cClass.getMethod("isReady")
            if (isReadyMethod.invoke(i2cInstance) == true) {
                onI2CReady(i2cInstance, i2cClass, callbackClass)
            }
        } catch (e: ClassNotFoundException) {
            Log.w(TAG, "eCarX I2C classes not available (emulator mode)", e)
        } catch (e: Exception) {
            Log.e(TAG, "I2C init error", e)
        }
    }

    private fun onI2CReady(i2cInstance: Any, i2cClass: Class<*>, callbackClass: Class<*>) {
        if (isListenerRegistered) return

        try {
            val registerMethod = i2cClass.getMethod("registerI2cListener", callbackClass)
            val i2cCallback = java.lang.reflect.Proxy.newProxyInstance(
                callbackClass.classLoader,
                arrayOf(callbackClass)
            ) { _, method, args ->
                if (method.name == "onReceiveI2cData") {
                    val data = args?.get(0) as? IntArray ?: return@newProxyInstance null
                    Log.d(TAG, "I2C data received: ${data.joinToString()}")
                    if (data.isNotEmpty() && data[0] == 16) {
                        processKeyData(data)
                    }
                }
                null
            }
            val success = registerMethod.invoke(i2cInstance, i2cCallback) as? Boolean ?: false
            if (success) {
                isListenerRegistered = true
                isInitialized = true
            }
        } catch (e: Exception) {
            Log.e(TAG, "I2C registerListener error", e)
        }
    }

    private fun processKeyData(data: IntArray) {
        synchronized(this) {
            val prev = previousData

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

            fun checkButton602(index: Int, keyCode: Int) {
                if (data.size <= index) return
                val cur = data[index]
                val pre = if (prev != null && prev.size > index) prev[index] else 0
                if ((cur and IICKeyCodes.IIC_BITMASK_602_PRESS) != 0 && (pre and IICKeyCodes.IIC_BITMASK_602_PRESS) == 0) {
                    Log.d(TAG, "I2C key event: keyCode=$keyCode isRelease=false")
                    handleKey(keyCode, false)
                } else if ((cur and IICKeyCodes.IIC_BITMASK_602_PRESS) == 0 && (pre and IICKeyCodes.IIC_BITMASK_602_PRESS) != 0) {
                    Log.d(TAG, "I2C key event: keyCode=$keyCode isRelease=true")
                    handleKey(keyCode, true)
                }
            }

            checkButton(IICKeyCodes.IIC_DATA_INDEX_VOLUME_UP, IICKeyCodes.KEY_CODE_IIC_VOLUME_UP, skip = flavor601)
            if (flavor602) {
                checkButton602(IICKeyCodes.IIC_DATA_INDEX_602_MUTE, IICKeyCodes.KEY_CODE_IIC_MUTE)
                checkButton602(IICKeyCodes.IIC_DATA_INDEX_602_POWER, IICKeyCodes.KEY_CODE_IIC_POWER)
            } else {
                checkButton(IICKeyCodes.IIC_DATA_INDEX_MUTE, IICKeyCodes.KEY_CODE_IIC_MUTE)
                checkButton(IICKeyCodes.IIC_DATA_INDEX_POWER, IICKeyCodes.KEY_CODE_IIC_POWER)
            }
            checkButton(IICKeyCodes.IIC_DATA_INDEX_VOLUME_DOWN, IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN, skip = flavor601)

            previousData = data.copyOf()
        }
    }

    private fun handleKey(keyCode: Int, isRelease: Boolean) {
        keyEventHandler?.invoke(keyCode, isRelease)
    }

    override fun release() {
        synchronized(this) {
            previousData = null
            isInitialized = false
        }
        isListenerRegistered = false
        i2cCommunication = null
        policy = null
    }

    override fun setKeyEventHandler(handler: (Int, Boolean) -> Unit) {
        keyEventHandler = handler
    }

    override fun isInitialized(): Boolean = isInitialized
}
