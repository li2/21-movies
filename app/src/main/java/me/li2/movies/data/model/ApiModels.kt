package me.li2.movies.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*
Postman mock server error response
{
    "error": {
        "name": "mockRequestNotFoundError",
        "message": "We were unable to find any matching requests for this method type and the mock path, '/api/auth/register', in your collection."
    }
}
*/

@JsonClass(generateAdapter = true)
data class ResponseAPI(
        @Json(name = "error")
        val error: Error?)

@JsonClass(generateAdapter = true)
data class Error(
        @Json(name = "message")
        val message: String?)

@JsonClass(generateAdapter = true)
data class MovieAPI(
        @Json(name = "id")
        val id: Long,
        @Json(name = "name")
        val name: String,
        @Json(name = "runningTime")
        val runningTime: String,
        @Json(name = "type")
        val type: String,
        @Json(name = "description")
        val description: String,
        @Json(name = "rate")
        val rate: Float?,
        @Json(name = "trailerUrl")
        val trailerUrl: String)
