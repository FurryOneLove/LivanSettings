package ru.who.livansetting.features.navi

/**
 * Сырое состояние навигации, собираемое из broadcast-интентов Яндекс.Навигатора.
 * Все поля приходят строками — Яндекс отдаёт их как текст ("1,2 км", "15 мин").
 * Преобразование в числа выполняется в [DimNaviData.fromState] через [NaviDecodeUtils].
 */
data class NaviState(
    /** Тип манёвра, напр. "context_ra_turn_left". */
    var maneuverDirection: String = "",
    /** Расстояние до манёвра, напр. "300 м" / "1,2 км". */
    var maneuverDistance: String = "",
    /** Название следующей дороги/улицы. */
    var maneuverNextRoad: String = "",
    /** Оставшееся время в пути, напр. "15 мин" / "1 ч 20 мин". */
    var routeEtaTime: String = "",
    /** Время прибытия (как строка от навигатора). */
    var routeEtaArrival: String = "",
    /** Расстояние до пункта назначения, напр. "12 км". */
    var routeEtaDistance: String = "",
    /** Признак завершения маршрута. */
    var finished: Boolean = false,
    /** Метка времени последнего события (мс). */
    var lastEventTs: Long = 0L
)
