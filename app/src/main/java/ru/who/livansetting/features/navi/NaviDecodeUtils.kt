package ru.who.livansetting.features.navi

/**
 * Преобразование строковых значений Яндекс.Навигатора в числа.
 * Яндекс отдаёт расстояния и время текстом ("1,2 км", "300 м", "15 мин", "1 ч 20 мин"),
 * а приборке нужны метры и секунды.
 */
object NaviDecodeUtils {

    /**
     * Метры из строки вида "1,2 км" / "300 м".
     * @param fallback значение по умолчанию, если распарсить не удалось.
     */
    fun metersFromString(s: String?, fallback: Int): Int {
        if (s.isNullOrBlank()) return fallback
        val v = s.trim().replace(",", ".")
        return try {
            when {
                v.endsWith(" км") -> {
                    val km = v.substring(0, v.length - 3).trim().toFloat()
                    Math.round(km * 1000f)
                }
                v.endsWith(" м") -> v.substring(0, v.length - 2).trim().toInt()
                else -> fallback
            }
        } catch (e: Exception) {
            fallback
        }
    }

    /**
     * Секунды из строки вида "15 мин" / "1 ч" / "1 ч 20 мин".
     */
    fun secondsFromString(s: String?, fallback: Int): Int {
        if (s.isNullOrBlank()) return fallback
        val v = s.trim()
        return try {
            // "X ч Y мин"
            if (v.contains(" ч ") && v.endsWith(" мин")) {
                val parts = v.replace(" мин", "").split(" ч ")
                val hours = parts[0].trim().toInt()
                val mins = parts[1].trim().toInt()
                hours * 3600 + mins * 60
            } else if (v.endsWith(" мин")) {
                v.substring(0, v.length - 4).trim().toInt() * 60
            } else if (v.endsWith(" ч")) {
                v.substring(0, v.length - 2).trim().toInt() * 3600
            } else {
                fallback
            }
        } catch (e: Exception) {
            fallback
        }
    }

    /**
     * Срезает приставку с названия улицы для компактного вывода на приборку:
     * "Улица Ленина" -> "Ленина", "проспект Мира" -> "Мира" и т.п.
     */
    fun removeStreetPrefix(input: String?): String {
        if (input.isNullOrBlank()) return "-"
        val s = input.trim()
        val prefixes = listOf(
            "Улица ", "улица ", "ул. ",
            "Проспект ", "проспект ", "просп. ", "пр. ",
            "Шоссе ", "шоссе ", "ш. ",
            "Переулок ", "переулок ", "пер. ",
            "Набережная ", "наб. реки ", "наб. ",
            "Бульвар ", "бульвар ", "бв. ",
            "Площадь ", "площадь ", "пл. ",
            "Проезд ", "проезд "
        )
        for (p in prefixes) {
            if (s.startsWith(p)) {
                val res = s.substring(p.length).trim()
                return if (res.length < 5) res.uppercase() else res
            }
        }
        return if (s.length < 5) s.uppercase() else s
    }
}
