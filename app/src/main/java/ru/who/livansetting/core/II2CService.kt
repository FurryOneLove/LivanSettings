package ru.who.livansetting.core

interface II2CService {
    fun initialize()
    fun setKeyEventHandler(handler: (Int, Boolean) -> Unit)
    fun release()
    fun isInitialized(): Boolean
}
