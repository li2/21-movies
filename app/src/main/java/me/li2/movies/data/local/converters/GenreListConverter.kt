package me.li2.movies.data.local.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import me.li2.movies.data.model.GenreUI

class GenreListConverter {
    private val json = Json(JsonConfiguration.Stable)

    @TypeConverter
    fun serializing(genres: List<GenreUI>): String {
        return json.stringify(GenreUI.serializer().list, genres)
    }

    @TypeConverter
    fun parsing(str: String): List<GenreUI> {
        return json.parse(GenreUI.serializer().list, str)
    }
}