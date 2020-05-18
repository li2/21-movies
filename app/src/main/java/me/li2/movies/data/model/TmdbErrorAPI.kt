/*
 * Created by Weiyi Li on 2020-05-02.
 * https://github.com/li2
 */
package me.li2.movies.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TmdbErrorAPI(
        @Json(name = "status_code")
        val errorCode: Int,
        @Json(name = "status_message")
        val errorMessage: String,
        @Json(name = "success")
        val success: Boolean?
)