/*
 * Created by Weiyi Li on 2020-04-25.
 * https://github.com/li2
 */
package me.li2.movies.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TmdbMovieListAPI(
        @Json(name = "page")
        val page: Int,
        @Json(name = "results")
        val results: List<TmdbMovieAPI>,
        @Json(name = "total_pages")
        val totalPages: Int,
        @Json(name = "total_results")
        val totalResults: Int
)

@JsonClass(generateAdapter = true)
data class TmdbMovieAPI(
        @Json(name = "adult")
        val adult: Boolean,
        @Json(name = "backdrop_path")
        val backdropPath: String?,
        @Json(name = "genre_ids")
        val genreIds: List<Int>,
        @Json(name = "id")
        val id: Int,
        @Json(name = "original_language")
        val originalLanguage: String,
        @Json(name = "original_title")
        val originalTitle: String,
        @Json(name = "overview")
        val overview: String,
        @Json(name = "popularity")
        val popularity: Double,
        @Json(name = "poster_path")
        val posterPath: String?,
        @Json(name = "release_date")
        val releaseDate: String?,
        @Json(name = "title")
        val title: String,
        @Json(name = "video")
        val video: Boolean,
        @Json(name = "vote_average")
        val voteAverage: Double,
        @Json(name = "vote_count")
        val voteCount: Int
)