package me.li2.movies.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TmdbMovieVideoListAPI(
        @Json(name = "id")
        val movieId: Int,
        @Json(name = "results")
        val results: List<TmdbMovieVideoAPI>
)

@JsonClass(generateAdapter = true)
data class TmdbMovieVideoAPI(
        @Json(name = "id")
        val id: String,
        @Json(name = "iso_3166_1")
        val iso31661: String,
        @Json(name = "iso_639_1")
        val iso6391: String,
        @Json(name = "key")
        val path: String,
        @Json(name = "name")
        val name: String,
        @Json(name = "site")
        val site: String,
        @Json(name = "size")
        val size: Int,
        @Json(name = "type")
        val type: String
)