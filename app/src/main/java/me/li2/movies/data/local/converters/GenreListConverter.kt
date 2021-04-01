/*
 * Created by Weiyi Li on 2020-05-04.
 * https://github.com/li2
 */
package me.li2.movies.data.local.converters

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.li2.movies.data.model.GenreUI

class GenreListConverter {
    private val json = Json { encodeDefaults = true }

    @TypeConverter
    fun serializing(genres: List<GenreUI>): String {
        return json.encodeToString(genres)
    }

    @TypeConverter
    fun parsing(str: String): List<GenreUI> {
        return json.decodeFromString(str)
    }
}