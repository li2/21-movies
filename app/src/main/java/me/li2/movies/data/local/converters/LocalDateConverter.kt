package me.li2.movies.data.local.converters

import androidx.room.TypeConverter
import me.li2.movies.util.formatIsoLocalDatePattern
import me.li2.movies.util.parseLocalDate
import org.threeten.bp.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun serializing(date: LocalDate?): String? {
        return date?.formatIsoLocalDatePattern()
    }

    @TypeConverter
    fun parsing(str: String?): LocalDate? {
        return str?.parseLocalDate()
    }
}
