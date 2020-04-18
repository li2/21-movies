package me.li2.movies.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TmdbMoviesListAPI(
        @Json(name = "page")
        val page: Int,
        @Json(name = "results")
        val results: List<TmdbMovieAPI>,
        @Json(name = "total_pages")
        val totalPages: Int,
        @Json(name = "total_results")
        val totalResults: Int
)