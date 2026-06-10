package ru.who.livansetting.features.navi

import ru.who.livansetting.data.SettingsManager

/**
 * Готовые к публикации на приборку данные (уже в метрах/секундах/turnId).
 * Получаются из [NaviState] через [fromState].
 */
data class DimNaviData(
    var status: Int = STATUS_NAVIGATING,
    var distanceToDestination: Long = 0L,
    var distanceToNextGuidancePoint: Long = 0L,
    var turnId: Int = TurnIdMapper.TURN_DEFAULT,
    var eta: Long = 0L,
    var nextGuidancePointName: String = ""
) {
    companion object {
        const val STATUS_UNNAVI = 0
        const val STATUS_NAVIGATING = 2

        /**
         * Преобразует сырое состояние навигатора в числовые данные для приборки.
         */
        fun fromState(state: NaviState, settings: SettingsManager): DimNaviData {
            val data = DimNaviData()

            data.status = if (state.finished) STATUS_UNNAVI else STATUS_NAVIGATING
            data.distanceToNextGuidancePoint =
                NaviDecodeUtils.metersFromString(state.maneuverDistance, 10).toLong()
            data.distanceToDestination =
                NaviDecodeUtils.metersFromString(state.routeEtaDistance, 100).toLong()
            data.eta = NaviDecodeUtils.secondsFromString(state.routeEtaTime, 0).toLong()
            data.nextGuidancePointName =
                NaviDecodeUtils.removeStreetPrefix(state.maneuverNextRoad)
            data.turnId = TurnIdMapper.toTurnId(state.maneuverDirection)

            // На финише дистанция до манёвра = дистанция до цели.
            if (data.turnId == TurnIdMapper.TURN_FINISH) {
                data.distanceToNextGuidancePoint = data.distanceToDestination
            }

            // Опция: гасить маршрут на финишной прямой при остатке < порога.
            val finishingEst = settings.getDimNaviFinishingEst()
            if (finishingEst > 0 && data.distanceToDestination < finishingEst) {
                data.status = STATUS_UNNAVI
            }

            return data
        }
    }
}
