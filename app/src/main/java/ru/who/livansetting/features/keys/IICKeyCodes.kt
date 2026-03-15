package ru.who.livansetting.features.keys

/**
 * Коды клавиш и индексы данных для I2C интерфейса
 */
object IICKeyCodes {
    // Коды клавиш для внутреннего использования
    const val KEY_CODE_IIC_VOLUME_UP = 3001
    const val KEY_CODE_IIC_VOLUME_DOWN = 3002
    const val KEY_CODE_IIC_MUTE = 3003
    const val KEY_CODE_IIC_POWER = 3004
    
    // Индексы в массиве данных I2C
    const val IIC_DATA_INDEX_VOLUME_UP = 1
    const val IIC_DATA_INDEX_MUTE = 2
    const val IIC_DATA_INDEX_VOLUME_DOWN = 3
    const val IIC_DATA_INDEX_POWER = 4
    
    // Маски для состояний (на основе анализа трафика)
    const val IIC_PRESS_MASK = 0x01
    const val IIC_RELEASE_MASK = 0x02
}
