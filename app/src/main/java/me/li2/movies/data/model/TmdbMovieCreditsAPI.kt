/*
 * Created by Weiyi Li on 2020-05-17.
 * https://github.com/li2
 */
package me.li2.movies.data.model

import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class TmdbMovieCreditListAPI(
        @Json(name = "cast")
        val casts: List<CastAPI>,
        @Json(name = "crew")
        val crews: List<CrewAPI>,
        @Json(name = "id")
        val id: Int)

@JsonClass(generateAdapter = true)
data class CastAPI(
        @Json(name = "cast_id")
        val castId: Int,
        @Json(name = "character")
        val character: String,
        @Json(name = "credit_id")
        val creditId: String,
        @Json(name = "gender")
        val gender: Int?,
        @Json(name = "id")
        val id: Int,
        @Json(name = "name")
        val name: String,
        @Json(name = "order")
        val order: Int,
        @Json(name = "profile_path")
        val profilePath: String?)

@JsonClass(generateAdapter = true)
data class CrewAPI(
        @Json(name = "credit_id")
        val creditId: String,
        @Json(name = "department")
        val department: String,
        @Json(name = "gender")
        val gender: Int?,
        @Json(name = "id")
        val id: Int,
        @Json(name = "job")
        val job: String,
        @Json(name = "name")
        val name: String,
        @Json(name = "profile_path")
        val profilePath: String?)