package ru.who.livansetting.features.navi

/**
 * Перевод строкового типа манёвра Яндекс.Навигатора в числовой turnId,
 * который понимает приборка (DIM).
 *
 * Значения turnId соответствуют тем, что используются в прошивке ECARX/Livan
 * (см. реверс реализации в сторонних приложениях):
 *   102 - поворот налево
 *   103 - поворот направо
 *   104 - принять левее / съезд левый
 *   105 - принять правее / съезд правый
 *   106 - резкий поворот налево
 *   107 - резкий поворот направо
 *   108 - разворот налево
 *   110 - промежуточная точка (via)
 *   111 - въезд в круговое движение
 *   112 - выезд из кругового движения
 *   115 - финиш
 *   119 - разворот направо
 *   120 - прямо / по умолчанию
 */
object TurnIdMapper {

    const val TURN_DEFAULT = 120
    const val TURN_FINISH = 115

    fun toTurnId(maneuverDirection: String?): Int {
        return when (maneuverDirection) {
            "context_ra_turn_left" -> 102
            "context_ra_turn_right" -> 103
            "context_ra_take_left", "context_ra_exit_left" -> 104
            "context_ra_take_right", "context_ra_exit_right" -> 105
            "context_ra_hard_turn_left" -> 106
            "context_ra_hard_turn_right" -> 107
            "context_ra_turn_back_left" -> 108
            "context_ra_via" -> 110
            "context_ra_in_circular_movement" -> 111
            "context_ra_out_circular_movement" -> 112
            "context_ra_finish" -> 115
            "context_ra_turn_back_right" -> 119
            else -> TURN_DEFAULT
        }
    }
}
