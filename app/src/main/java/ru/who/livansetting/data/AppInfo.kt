package ru.who.livansetting.data

import android.graphics.drawable.Drawable

/**
 * Модель данных для приложения
 */
data class AppInfo(
    val packageName: String,
    val appName: String,
    val icon: Drawable? = null
)
