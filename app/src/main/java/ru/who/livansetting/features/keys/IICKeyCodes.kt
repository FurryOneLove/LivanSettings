package ru.who.livansetting.features.keys

object IICKeyCodes {
    const val KEY_CODE_IIC_VOLUME_UP = 3001
    const val KEY_CODE_IIC_VOLUME_DOWN = 3002
    const val KEY_CODE_IIC_MUTE = 3003
    const val KEY_CODE_IIC_POWER = 3004

    const val IIC_DATA_INDEX_VOLUME_UP = 1
    const val IIC_DATA_INDEX_MUTE = 2
    const val IIC_DATA_INDEX_VOLUME_DOWN = 3
    const val IIC_DATA_INDEX_POWER = 4

    // ihu602-specific data indices (different layout than standard, uses bitmask detection)
    // Note: IIC_DATA_INDEX_602_MUTE = 4 intentionally shares the numeric value with IIC_DATA_INDEX_POWER.
    // These are device-exclusive: ihu602 uses this branch, non-602 devices use IIC_DATA_INDEX_POWER.
    const val IIC_DATA_INDEX_602_MUTE = 4
    const val IIC_DATA_INDEX_602_POWER = 5
    const val IIC_BITMASK_602_PRESS = 0x40
}
