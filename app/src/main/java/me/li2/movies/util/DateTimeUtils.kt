package me.li2.movies.util

import android.text.format.DateFormat
import org.threeten.bp.DateTimeException
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import timber.log.Timber.e
import java.util.*

fun String.parseLocalDate(): LocalDate? {
    return try {
        LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (exception: DateTimeParseException) {
        e(exception, "failed to parse date: $this")
        null
    }
}

fun LocalDate.formatIsoLocalDatePattern(): String? {
    return try {
        this.format(DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (exception: DateTimeException) {
        e(exception, "failed to format date: $this")
        null
    }
}

fun LocalDate.formatBestLocalPattern(pattern: String): String {
    return try {
        val locale: Locale = Locale.getDefault()
        val bestPattern = DateFormat.getBestDateTimePattern(locale, pattern)
        this.format(DateTimeFormatter.ofPattern(bestPattern, locale))
    } catch (exception: Exception) {
        e(exception, "failed to format datetime: $this with pattern: $pattern")
        this.toString()
    }
}

fun String.releaseDateWithBestLocalPattern(): String {
    return this.parseLocalDate()?.formatBestLocalPattern("yyyy-MM-dd") ?: this
}