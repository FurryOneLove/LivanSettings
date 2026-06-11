package ru.who.livansetting.features.music

/**
 * Готовые данные о воспроизведении для приборки (DIM).
 * Лимиты длины полей подобраны под размеры виджета на приборке
 * (как в реализации Lunaris).
 */
data class DimMusicData(
    var title: String = "",
    var artist: String = "",
    var album: String = "",
    var durationMs: Long = 0L,
    var positionMs: Long = 0L,
    /** 1 — играет, 0 — пауза. */
    var playbackStatus: Int = 0,
    /** Тип источника DIM (SOURCE_TYPE_ONLINE = 6 по умолчанию). */
    var sourceType: Int = 6,
    /** Идентификатор для дедупликации (чтобы не слать одно и то же). */
    var uuid: String = ""
) {
    fun limitedTitle(): String = title.take(TITLE_MAX)
    fun limitedArtist(): String = artist.take(ARTIST_MAX)
    fun limitedAlbum(): String = album.take(ALBUM_MAX)

    companion object {
        const val TITLE_MAX = 16
        const val ARTIST_MAX = 11
        const val ALBUM_MAX = 10

        const val STATUS_PAUSED = 0
        const val STATUS_PLAYING = 1

        const val SOURCE_TYPE_ONLINE = 6

        /** Приветственный экран при старте (как «Добро пожаловать» в Lunaris). */
        fun welcome(): DimMusicData = DimMusicData(
            title = "Добро пожаловать",
            artist = "(^_^)",
            album = "Livan",
            playbackStatus = STATUS_PAUSED,
            uuid = "welcome"
        )
    }
}
