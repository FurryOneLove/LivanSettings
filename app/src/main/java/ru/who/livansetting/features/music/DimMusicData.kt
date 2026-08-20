package ru.who.livansetting.features.music

/**
 * Готовые данные о воспроизведении для приборки (DIM).
 *
 * ВАЖНО про [sourceType]. MediaInteractionImpl.updatePlaybackInfo() устроен так
 * (switch без break у музыкальной ветки — проверено по байткоду и по логам):
 *   3 (FM), 4 (AM), 0x21, 0x22    -> радийная ветка, updateMediaPlayInfo НЕ вызывается
 *   0 (LOCAL), 1 (USB), 7 (USB2)  -> updateMediaPlayInfo вызывается ДВАЖДЫ
 *                                    (case проваливается в default) — двойной трафик в CAN
 *   всё прочее (2 BT, 6 ONLINE …) -> updateMediaPlayInfo вызывается один раз
 * То есть в музыку уходит любой тип, кроме четырёх радийных.
 * Берём 6 (ONLINE): семантически верно для стриминга и вдвое меньше кадров.
 * Значение попадает в младший нибл первого байта каждого блока после
 * convertMusicType2IPK(): 6 -> 9 (ONLINE), 1 -> 2 (USB), 0 -> 3 (HDD), 2 -> 5 (BT).
 *
 * Лимиты длины полей подобраны под размеры виджета на приборке.
 * Само API дополнительно режет каждое поле до 31 символа и кодирует UTF-16LE.
 */
data class DimMusicData(
    var title: String = "",
    var artist: String = "",
    var album: String = "",
    var durationMs: Long = 0L,
    /**
     * Позиция трека. На приборку НЕ передаётся:
     * IMediaInteraction.updateCurrentProgress() в прошивке — пустой метод,
     * а первые четыре аргумента convertMusicF7() захардкожены нулями.
     * Поле оставлено только для отладочных экранов.
     */
    var positionMs: Long = 0L,
    /** 1 — играет, 0 — пауза. */
    var playbackStatus: Int = 0,
    /** Тип источника DIM. См. комментарий к классу. */
    var sourceType: Int = SOURCE_TYPE_ONLINE,
    /** Идентификатор для дедупликации (чтобы не слать одно и то же). */
    var uuid: String = ""
) {
    fun limitedTitle(): String = title.takeCodePoints(TITLE_MAX)
    fun limitedArtist(): String = artist.takeCodePoints(ARTIST_MAX)
    fun limitedAlbum(): String = album.takeCodePoints(ALBUM_MAX)

    /**
     * Подпись содержимого — по ПОЛНЫМ строкам, до обрезки и прокрутки.
     * Меняется только при реальной смене трека или статуса, поэтому по ней
     * решаем, надо ли сбрасывать бегущую строку в начало.
     */
    fun signature(): String = "$title|$artist|$album|$playbackStatus|$sourceType"

    companion object {
        const val TITLE_MAX = 16
        const val ARTIST_MAX = 11
        const val ALBUM_MAX = 10

        const val STATUS_PAUSED = 0
        const val STATUS_PLAYING = 1

        // Типы источника из IMediaInteraction.
        /** Локальный носитель. -> IPK 3 (HDD). ВНИМАНИЕ: двойная отправка. */
        const val SOURCE_TYPE_LOCAL = 0

        /** USB. -> IPK 2 (USB). ВНИМАНИЕ: двойная отправка. */
        const val SOURCE_TYPE_USB = 1

        /** Bluetooth. -> IPK 5 (BT). Одна отправка. */
        const val SOURCE_TYPE_BT = 2

        /** Онлайн/стриминг. -> IPK 9 (ONLINE). Одна отправка. Используем по умолчанию. */
        const val SOURCE_TYPE_ONLINE = 6

        /** Второй USB. -> IPK 2 (USB). ВНИМАНИЕ: двойная отправка. */
        const val SOURCE_TYPE_USB2 = 7

        /**
         * «Источника нет». Единственное значение, которое обрабатывает
         * updateCurrentSourceType() — все остальные он игнорирует.
         * На приборке даёт IPK MUSIC_TYPE 8 (DISCONNECT).
         */
        const val SOURCE_TYPE_DISCONNECT = -1
    }
}

/**
 * Обрезка по code points, а не по UTF-16 code units:
 * иначе эмодзи или любой символ вне BMP рвётся пополам
 * и на приборке появляется мусор.
 */
private fun String.takeCodePoints(max: Int): String {
    if (max <= 0 || isEmpty()) return ""
    var i = 0
    var n = 0
    while (i < length && n < max) {
        i += Character.charCount(codePointAt(i))
        n++
    }
    return if (i >= length) this else substring(0, i)
}
