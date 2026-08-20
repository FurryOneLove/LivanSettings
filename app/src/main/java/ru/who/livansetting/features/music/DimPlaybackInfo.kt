package ru.who.livansetting.features.music

import java.lang.reflect.Proxy

/**
 * Создаёт объект, реализующий интерфейс
 * com.ecarx.xui.adaptapi.diminteraction.IMediaInteraction.IPlaybackInfo,
 * который приборка опрашивает геттерами.
 *
 * Интерфейс — системный eCarX-класс, поэтому реализуем через динамический Proxy
 * (как и для навигации), чтобы проект собирался без системного SDK и не падал
 * в эмуляторе.
 *
 * ДИАГНОСТИКА: при [verbose] = true каждый вызов геттера пишется в FileLog.
 * Так видно главное — опрашивает ли приборка наш объект вообще. Если после
 * updatePlaybackInfo в логе нет ни одной строки «PI ->», значит DIM данные
 * не забирает, и дело не в содержимом, а в самом подключении источника.
 *
 * Что из этих геттеров реально доезжает до CAN (проверено по AdapterAPIImpl):
 *   getSourceType      -> выбор ветки (музыка/радио/ничего) и младший нибл
 *                         первого байта каждого блока кадра 0x01
 *   getTitle/Artist/Album -> текст кадра 0x01, UTF-16LE, до 31 символа каждый
 *   getPlaybackStatus  -> кадр 0x07 (convertMusicStatus2IPK)
 *   getUUID/getArtwork -> только broadcast обложки, на текст не влияют
 *   остальные          -> не используются, но интерфейс требует их реализовать
 */
object DimPlaybackInfo {

    private const val TAG = "DimPlaybackInfo"
    private const val INTERFACE =
        "com.ecarx.xui.adaptapi.diminteraction.IMediaInteraction\$IPlaybackInfo"

    /** Включается кнопкой «Геттеры» в окне лога. */
    @Volatile
    var verbose: Boolean = false

    fun createProxy(data: DimMusicData): Any {
        val infoClass = Class.forName(INTERFACE)

        return Proxy.newProxyInstance(
            infoClass.classLoader,
            arrayOf(infoClass)
        ) { proxy, method, args ->
            val result: Any? = when (method.name) {
                "getTitle" -> data.limitedTitle()
                "getArtist" -> data.limitedArtist()
                "getAlbum" -> data.limitedAlbum()
                "getPlaybackStatus" -> data.playbackStatus
                "getDuration" -> data.durationMs
                "getSourceType" -> data.sourceType
                "getUUID" -> data.uuid
                // Ниже — заглушки. Строки возвращаем пустыми, а не null:
                // AdaptAPI местами склеивает их в StringBuilder и сравнивает.
                "getCurrentLyricSentence" -> ""
                "getLyricContent" -> ""
                "getRadioStationName" -> ""
                "getRadioFrequency" -> ""
                "getRadioMode" -> 0
                "getLoopMode" -> 0
                "getFavoriteState" -> 0
                "getPlayingItemPositionInQueue" -> 0
                "getArtwork" -> null
                "getNextArtwork" -> null
                "getPreviousArtwork" -> null
                "getMediaPath" -> null
                "getLyric" -> null
                "hashCode" -> System.identityHashCode(proxy)
                "equals" -> proxy === args?.getOrNull(0)
                "toString" -> "DimPlaybackInfo($data)"
                else -> {
                    // Неизвестный геттер — раньше уходило в Log.d и терялось.
                    FileLog.w(TAG, "неизвестный геттер: ${method.name} : ${method.returnType.simpleName}")
                    defaultFor(method.returnType)
                }
            }

            if (verbose && method.name != "hashCode" && method.name != "toString") {
                FileLog.raw("    PI -> ${method.name}() = ${short(result)}")
            }
            result
        }
    }

    private fun short(v: Any?): String {
        val s = v?.toString() ?: "null"
        return if (s.length > 60) s.take(60) + "…" else s
    }

    private fun defaultFor(type: Class<*>): Any? = when (type) {
        Int::class.javaPrimitiveType -> 0
        Long::class.javaPrimitiveType -> 0L
        Boolean::class.javaPrimitiveType -> false
        Float::class.javaPrimitiveType -> 0f
        Double::class.javaPrimitiveType -> 0.0
        String::class.java -> ""
        else -> null
    }
}
