/*
 * Created by Weiyi Li on 2020-05-11.
 * https://github.com/li2
 */
package me.li2.movies.fcm

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.android.parcel.Parcelize
import timber.log.Timber

@JsonClass(generateAdapter = true)
@Parcelize
data class MessageAPI(
        @Json(name = "id")
        val id: String,
        @Json(name = "title")
        val title: String,
        @Json(name = "release_date")
        val releaseDate: String,
        @Json(name = "poster_path")
        val posterUrl: String,
        @Json(name = "overview")
        val overview: String) : Parcelable {

    companion object {
        fun fromJson(json: String): MessageAPI? {
            return try {
                Moshi.Builder().build().adapter(MessageAPI::class.java).fromJson(json)
            } catch (exception: Exception) {
                Timber.e(exception, "failed to parse $json")
                null
            }
        }
    }
}