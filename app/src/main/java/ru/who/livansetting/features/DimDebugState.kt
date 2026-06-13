package ru.who.livansetting.features

/**
 * Отладочное состояние DIM-функций для отображения прямо в приложении
 * (без ADB и без активной приборки).
 *
 * Менеджеры (навигация, музыка) записывают сюда статус подключения и последние
 * отправленные данные, а экраны настроек читают и показывают пользователю.
 *
 * Это позволяет убедиться, что код собирает и отправляет данные правильно,
 * даже если приборка ничего не отображает.
 */
object DimDebugState {

    // --- Навигация ---

    /** Состояние подключения к приборке: NONE / CONNECTED / NULL / ERROR. */
    @Volatile
    var naviConnection: String = "не инициализировано"

    /** Последняя отправленная на приборку строка данных навигации. */
    @Volatile
    var naviLastPublished: String = "—"

    /** Последняя ошибка публикации навигации (или пусто). */
    @Volatile
    var naviLastError: String = ""

    /** Счётчик успешных публикаций навигации. */
    @Volatile
    var naviPublishCount: Int = 0

    // --- Музыка ---

    @Volatile
    var musicConnection: String = "не инициализировано"

    /** Статус доступа к уведомлениям (нужен для чтения медиа-сессий). */
    @Volatile
    var musicNotifAccess: String = "—"

    @Volatile
    var musicLastPublished: String = "—"

    @Volatile
    var musicLastError: String = ""

    @Volatile
    var musicPublishCount: Int = 0

    /** Последняя найденная активная медиа-сессия (пакет приложения). */
    @Volatile
    var musicActiveSession: String = "—"

    fun resetNavi() {
        naviLastPublished = "—"
        naviLastError = ""
        naviPublishCount = 0
    }

    fun resetMusic() {
        musicLastPublished = "—"
        musicLastError = ""
        musicPublishCount = 0
        musicActiveSession = "—"
    }
}
