package me.li2.movies.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TmdbMovieReviewListAPI(
        @Json(name = "id")
        val movieId: Int,
        @Json(name = "page")
        val page: Int,
        @Json(name = "results")
        val results: List<TmdbMovieReviewAPI>,
        @Json(name = "total_pages")
        val totalPages: Int,
        @Json(name = "total_results")
        val totalResults: Int
)

@JsonClass(generateAdapter = true)
data class TmdbMovieReviewAPI(
        @Json(name = "author")
        val author: String,
        @Json(name = "content")
        val content: String,
        @Json(name = "id")
        val id: String,
        @Json(name = "url")
        val url: String
)