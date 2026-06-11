package ru.who.livansetting.features.music

import android.util.Log
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
 * Сопоставление геттеров (по IMediaInteraction.IPlaybackInfo):
 *   getTitle / getArtist / getAlbum  -> данные трека (с лимитами длины)
 *   getPlaybackStatus                -> 1 играет / 0 пауза
 *   getDuration                      -> длительность, мс
 *   getSourceType                    -> тип источника
 *   getUUID                          -> идентификатор для дедупликации
 *   остальные (лирика, радио, обложки, очередь) -> пустые значения
 */
object DimPlaybackInfo {

    private const val TAG = "DimPlaybackInfo"
    private const val INTERFACE =
        "com.ecarx.xui.adaptapi.diminteraction.IMediaInteraction\$IPlaybackInfo"

    fun createProxy(data: DimMusicData): Any {
        val infoClass = Class.forName(INTERFACE)

        return Proxy.newProxyInstance(
            infoClass.classLoader,
            arrayOf(infoClass)
        ) { proxy, method, _ ->
            when (method.name) {
                "getTitle" -> data.limitedTitle()
                "getArtist" -> data.limitedArtist()
                "getAlbum" -> data.limitedAlbum()
                "getPlaybackStatus" -> data.playbackStatus
                "getDuration" -> data.durationMs
                "getSourceType" -> data.sourceType
                "getUUID" -> data.uuid
                "getCurrentLyricSentence" -> null
                "getLyricContent" -> ""
                "getRadioStationName" -> ""
                "getRadioFrequency" -> null
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
                "equals" -> proxy === null
                "toString" -> "DimPlaybackInfo($data)"
                else -> {
                    Log.d(TAG, "Unhandled getter: ${method.name}")
                    defaultFor(method.returnType)
                }
            }
        }
    }

    private fun defaultFor(type: Class<*>): Any? = when (type) {
        Int::class.javaPrimitiveType -> 0
        Long::class.javaPrimitiveType -> 0L
        Boolean::class.javaPrimitiveType -> false
        else -> null
    }
}
