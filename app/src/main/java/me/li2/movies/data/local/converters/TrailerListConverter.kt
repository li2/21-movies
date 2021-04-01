/*
 * Created by Weiyi Li on 2020-05-04.
 * https://github.com/li2
 */
package me.li2.movies.data.local.converters

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.li2.movies.data.model.Trailer

class TrailerListConverter {
    private val json = Json { encodeDefaults = true }

    @TypeConverter
    fun serializing(trailers: List<Trailer>): String {
        return json.encodeToString(trailers)
    }

    @TypeConverter
    fun parsing(str: String): List<Trailer> {
        return json.decodeFromString(str)
    }
}