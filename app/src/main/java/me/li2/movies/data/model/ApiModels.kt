package me.li2.movies.data.model

import com.squareup.moshi.Json

/*
Postman mock server error response
{
    "error": {
        "name": "mockRequestNotFoundError",
        "message": "We were unable to find any matching requests for this method type and the mock path, '/api/auth/register', in your collection."
    }
}
*/
data class ResponseAPI(
        @Json(name = "error")
        val error: Error?)

data class Error(
        @Json(name = "message")
        val message: String?)

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
