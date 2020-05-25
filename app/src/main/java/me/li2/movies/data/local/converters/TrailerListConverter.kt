/*
 * Created by Weiyi Li on 2020-05-04.
 * https://github.com/li2
 */
package me.li2.movies.data.local.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import me.li2.movies.data.model.Trailer

class TrailerListConverter {
    private val json = Json(JsonConfiguration.Stable)

    @TypeConverter
    fun serializing(trailers: List<Trailer>): String {
        return json.stringify(Trailer.serializer().list, trailers)
    }

    @TypeConverter
    fun parsing(str: String): List<Trailer> {
        return json.parse(Trailer.serializer().list, str)
    }
}