/*
 * Created by Weiyi Li on 2020-06-01.
 * https://github.com/li2
 */
package me.li2.movies.data.model
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class GenresAPI(
    @Json(name = "genres")
    val genres: List<Genre>
)
