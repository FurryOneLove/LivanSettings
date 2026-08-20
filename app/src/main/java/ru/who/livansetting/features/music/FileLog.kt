package ru.who.livansetting.features.music

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Лог для диагностики в машине.
 *
 * ГЛАВНОЕ ОТЛИЧИЕ ОТ ПРЕДЫДУЩЕЙ ВЕРСИИ:
 * все строки всегда падают в кольцевой буфер в памяти — даже если init()
 * не вызывался и файл создать не удалось. Именно поэтому раньше «ничего
 * не снялось»: без init() writeLine() выходил на первой строке и не писал
 * вообще никуда.
 *
 * Читать лог теперь можно прямо в приложении: LogViewerActivity.
 * Файл (если получится создать) пишется дополнительно, как было:
 *  1) на флешку — /storage/XXXX-XXXX/Android/data/ru.who.livansetting/files/livan-dim.log
 *  2) иначе     — /sdcard/Android/data/ru.who.livansetting/files/livan-dim.log
 */
object FileLog {

    private const val FILE_NAME = "livan-dim.log"
    private const val MAX_BYTES = 512 * 1024L

    /** Сколько последних строк держим в памяти для показа на экране. */
    private const val MEM_LINES = 1000

    private val stamp = SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US)
    private val lock = Any()

    /** Кольцевой буфер — источник данных для экрана лога. */
    private val buffer = ArrayDeque<String>()

    @Volatile
    private var target: File? = null

    @Volatile
    private var initialized = false

    /** Растёт при каждой новой строке — экран по нему понимает, что пора перерисоваться. */
    @Volatile
    var revision: Long = 0L
        private set

    /** Вызывается один раз при старте. Повторные вызовы безвредны. */
    fun init(context: Context) {
        if (initialized) return
        synchronized(lock) {
            if (initialized) return
            initialized = true
            target = try {
                resolveFile(context)
            } catch (e: Exception) {
                Log.w("FileLog", "file unavailable: ${e.message}")
                null
            }
        }
        writeLine("=== log started ===")
        writeLine("device: ${Build.MANUFACTURER} ${Build.MODEL}, Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
        writeLine("file:   ${target?.absolutePath ?: "(только память)"}")
    }

    /** Полный путь к файлу — удобно показать в UI приложения. */
    fun path(): String = target?.absolutePath ?: "(файл не создан, лог только в памяти)"

    fun d(tag: String, msg: String) {
        Log.d(tag, msg); writeLine("D/$tag: $msg")
    }

    fun i(tag: String, msg: String) {
        Log.i(tag, msg); writeLine("I/$tag: $msg")
    }

    fun w(tag: String, msg: String, tr: Throwable? = null) {
        if (tr != null) Log.w(tag, msg, tr) else Log.w(tag, msg)
        writeLine("W/$tag: $msg${trace(tr)}")
    }

    fun e(tag: String, msg: String, tr: Throwable? = null) {
        if (tr != null) Log.e(tag, msg, tr) else Log.e(tag, msg)
        writeLine("E/$tag: $msg${trace(tr)}")
    }

    // ---------- чтение из приложения ----------

    /** Весь буфер одной строкой, старые записи сверху. */
    fun dump(): String = synchronized(lock) { buffer.joinToString("\n") }

    /** Последние n строк — чтобы не тормозить отрисовку на огромном логе. */
    fun tail(n: Int): String = synchronized(lock) {
        if (buffer.size <= n) buffer.joinToString("\n")
        else buffer.toList().subList(buffer.size - n, buffer.size).joinToString("\n")
    }

    fun size(): Int = synchronized(lock) { buffer.size }

    fun clear() {
        synchronized(lock) {
            buffer.clear()
            revision++
            try {
                target?.let { if (it.exists()) it.delete() }
            } catch (_: Exception) {
            }
        }
    }

    /** Записать произвольную строку без тега — для самодиагностики. */
    fun raw(text: String) = writeLine(text)

    /** Сбросить буфер в файл принудительно. Возвращает путь либо null. */
    fun flushToFile(context: Context): String? {
        if (target == null) {
            synchronized(lock) {
                target = try {
                    resolveFile(context)
                } catch (e: Exception) {
                    null
                }
            }
        }
        val f = target ?: return null
        return try {
            FileWriter(f, false).use { w ->
                w.append(dump()); w.append('\n'); w.flush()
            }
            f.absolutePath
        } catch (e: Exception) {
            Log.w("FileLog", "flush failed: ${e.message}")
            null
        }
    }

    // ---------- внутреннее ----------

    private fun trace(tr: Throwable?): String {
        if (tr == null) return ""
        val sw = StringWriter()
        tr.printStackTrace(PrintWriter(sw))
        return "\n$sw"
    }

    private fun writeLine(text: String) {
        val line = stamp.format(Date()) + "  " + text
        synchronized(lock) {
            buffer.addLast(line)
            while (buffer.size > MEM_LINES) buffer.removeFirst()
            revision++

            val f = target ?: return
            try {
                if (f.length() > MAX_BYTES) rotate(f)
                FileWriter(f, true).use { w ->
                    w.append(line)
                    w.append('\n')
                    w.flush()
                }
            } catch (e: Exception) {
                Log.w("FileLog", "write failed: ${e.message}")
            }
        }
    }

    private fun rotate(f: File) {
        try {
            val old = File(f.parentFile, "$FILE_NAME.1")
            if (old.exists()) old.delete()
            f.renameTo(old)
        } catch (_: Exception) {
        }
    }

    /**
     * Ищем съёмный носитель. getExternalFilesDirs возвращает каталоги приложения
     * на всех томах: [0] — встроенная память, дальше SD-карта и USB.
     */
    private fun resolveFile(context: Context): File {
        val dirs = try {
            context.getExternalFilesDirs(null).filterNotNull()
        } catch (e: Exception) {
            emptyList()
        }

        val removable = dirs.firstOrNull { dir ->
            try {
                Environment.isExternalStorageRemovable(dir)
            } catch (e: Exception) {
                false
            }
        }

        val dir = removable ?: dirs.firstOrNull() ?: context.filesDir
        try {
            if (!dir.exists()) dir.mkdirs()
        } catch (_: Exception) {
        }
        return File(dir, FILE_NAME)
    }
}
