package ru.who.livansetting.features.music

/**
 * Бегущая строка для одного поля приборки.
 *
 * Приборка показывает ровно то, что мы ей отправили, поэтому анимация делается
 * на нашей стороне: если строка длиннее окна [width], на каждом такте отдаём
 * окно, сдвинутое на один символ. Дойдя до конца, строка заворачивается через
 * разделитель [gap] обратно к началу — получается кольцо.
 *
 * Про «полный круг»: после того как окно вернулось в нулевое положение,
 * один такт стоим на месте. Так начало строки успевает прочитаться, а не
 * проскакивает мимо.
 *
 * Сдвиг считается по code points, а не по UTF-16 code units — иначе символы
 * вне BMP рвались бы пополам.
 *
 * Класс не потокобезопасен: предполагается вызов из одного handler'а.
 */
class DimMarquee(
    private val width: Int,
    private val gap: String = GAP
) {
    /** Исходная строка целиком. */
    private var full: String = ""

    /** Кольцо: символы строки + разделитель. Пусто, если прокрутка не нужна. */
    private var ring: List<String> = emptyList()

    /** Готовое окно, когда прокрутка не нужна. */
    private var still: String = ""

    private var offset: Int = 0

    /** true — на этом такте стоим на месте (пауза после полного круга). */
    private var hold: Boolean = false

    /** Нужна ли прокрутка: строка длиннее окна. */
    val scrolling: Boolean
        get() = ring.isNotEmpty()

    /**
     * Задать текст. Если он не изменился, позиция прокрутки сохраняется,
     * иначе анимация начинается заново.
     */
    fun setText(text: String) {
        if (text == full) return
        full = text
        val cps = text.codePointList()
        if (cps.size > width) {
            ring = cps + gap.codePointList()
            still = ""
        } else {
            ring = emptyList()
            still = text
        }
        offset = 0
        hold = false
    }

    /** Текущее окно шириной [width] (или вся строка, если она короче). */
    fun current(): String {
        if (ring.isEmpty()) return still
        val sb = StringBuilder(width)
        for (i in 0 until width) {
            sb.append(ring[(offset + i) % ring.size])
        }
        return sb.toString()
    }

    /** Шаг анимации. Для коротких строк ничего не делает. */
    fun advance() {
        if (ring.isEmpty()) return
        if (hold) {
            hold = false
            return
        }
        offset++
        if (offset >= ring.size) {
            offset = 0
            hold = true
        }
    }

    /** Вернуть анимацию в начало, не трогая текст. */
    fun rewind() {
        offset = 0
        hold = false
    }

    companion object {
        /** Разделитель между концом строки и её началом при заворачивании. */
        private const val GAP = "   "

        private fun String.codePointList(): List<String> {
            if (isEmpty()) return emptyList()
            val out = ArrayList<String>(length)
            var i = 0
            while (i < length) {
                val n = Character.charCount(codePointAt(i))
                out.add(substring(i, i + n))
                i += n
            }
            return out
        }
    }
}
