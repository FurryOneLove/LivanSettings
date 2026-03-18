package ru.who.livansetting.utils

import android.os.Build

object EmulatorDetector {
    val isEmulator: Boolean by lazy {
        Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.contains(":sdk_")
            || Build.FINGERPRINT.lowercase().contains("emulator")
            || Build.MODEL.contains("google_sdk", ignoreCase = true)
            || Build.MODEL.contains("Emulator", ignoreCase = true)
            || Build.MODEL.contains("Android SDK built for x86", ignoreCase = true)
            || Build.MANUFACTURER.contains("Genymotion", ignoreCase = true)
            || Build.BRAND.startsWith("generic")
            || Build.DEVICE.startsWith("generic")
            || Build.PRODUCT.contains("sdk_gphone", ignoreCase = true)
            || Build.HARDWARE.contains("goldfish", ignoreCase = true)
            || Build.HARDWARE.contains("ranchu", ignoreCase = true)
    }
}
