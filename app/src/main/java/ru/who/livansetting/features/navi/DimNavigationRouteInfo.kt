package ru.who.livansetting.features.navi

import android.util.Log
import ru.who.livansetting.data.SettingsManager
import java.lang.reflect.Proxy

/**
 * Создаёт объект, реализующий интерфейс
 * com.ecarx.xui.adaptapi.diminteraction.INaviInteraction.INavigationInfo,
 * который приборка опрашивает геттерами.
 *
 * Интерфейс — системный eCarX-класс, поэтому в обычной среде сборки его нет.
 * Реализуем через динамический Proxy (тот же приём, что и для IKeyCallback
 * в MainService), чтобы код собирался и не падал в эмуляторе.
 *
 * Сопоставление геттеров (по INaviInteraction.INavigationInfo):
 *   getNavigationStatus()            -> status (0 = нет навигации, 2 = ведём)
 *   getNavigationTurnId()            -> turnId
 *   getDistanceToNextGuidancePoint() -> метры до манёвра
 *   getDistanceToDestination()       -> метры до цели
 *   getETA()                         -> секунды
 *   getNextGuidancePointName()       -> название след. дороги (с лимитом длины)
 *   getDayNightMode() / getMuteState() / getDrivingDirection() -> простые значения
 *   getLaneInfo()                    -> пустой массив (полосы не используем)
 *   getHighwayExitInfo()/getRoadCameraInfo()/getServiceAreaInfo() -> null
 */
object DimNavigationRouteInfo {

    private const val TAG = "DimNavRouteInfo"
    private const val INTERFACE =
        "com.ecarx.xui.adaptapi.diminteraction.INaviInteraction\$INavigationInfo"

    fun createProxy(data: DimNaviData): Any {
        val infoClass = Class.forName(INTERFACE)
        val nameLimit = SettingsManager.DIM_NAVI_STREET_NAME_LIMIT

        return Proxy.newProxyInstance(
            infoClass.classLoader,
            arrayOf(infoClass)
        ) { proxy, method, _ ->
            when (method.name) {
                "getNavigationStatus" -> data.status
                "getNavigationTurnId" -> data.turnId
                "getNavigationTurnSVG" -> ""
                "getDistanceToNextGuidancePoint" -> data.distanceToNextGuidancePoint
                "getDistanceToDestination" -> data.distanceToDestination
                "getETA" -> data.eta
                "getDayNightMode" -> 0
                "getMuteState" -> 0
                "getDrivingDirection" -> 3 // DIRECTION_NORTH
                "getNextGuidancePointName" -> {
                    val n = data.nextGuidancePointName
                    if (n.length > nameLimit) n.substring(0, nameLimit) else n
                }
                "getLaneInfo" -> {
                    // Возвращаем пустой массив правильного типа ILaneInfo[].
                    val laneClass = Class.forName(
                        "com.ecarx.xui.adaptapi.diminteraction.INaviInteraction\$ILaneInfo"
                    )
                    java.lang.reflect.Array.newInstance(laneClass, 0)
                }
                "getHighwayExitInfo" -> null
                "getRoadCameraInfo" -> null
                "getServiceAreaInfo" -> null
                "hashCode" -> System.identityHashCode(proxy)
                "equals" -> proxy === null
                "toString" -> "DimNavigationRouteInfo($data)"
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
