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
    val error: Error?
)

data class Error(
    @Json(name = "message")
    val message: String?
)
