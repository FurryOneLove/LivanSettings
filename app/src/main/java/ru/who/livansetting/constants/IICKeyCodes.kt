package ru.who.livansetting.constants

import ru.who.livansetting.utils.BuildPropUtils

/**
 * Константы для IIC клавиш
 * Основаны на коде из InputService
 */
object IICKeyCodes {
    // IIC клавиши громкости и управления
    const val KEY_CODE_IIC_VOLUME_UP = 200024
    const val KEY_CODE_IIC_MUTE = 200164
    const val KEY_CODE_IIC_VOLUME_DOWN = 200025
    const val KEY_CODE_IIC_POWER = 26 // Используем стандартный KEYCODE_POWER
    
    // Маски для определения нажатия/отпускания клавиш
    const val IIC_PRESS_MASK = 64
    const val IIC_RELEASE_MASK = 32
    
    // Индексы данных в массиве I2C
    const val IIC_DATA_INDEX_VOLUME_UP = 2
    val IIC_DATA_INDEX_MUTE = if (BuildPropUtils.isFlavorContains("601")) 4 else 3
    const val IIC_DATA_INDEX_VOLUME_DOWN = 4
    const val IIC_DATA_INDEX_POWER = 5
}
