package me.li2.movies.ui.movies

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import timber.log.Timber.e

@Parcelize
enum class MoviesType : Parcelable {
    NOT_SHOWING,
    COMING_SOON
}

@Parcelize
data class MovieItem(
        val id: Long,
        val name: String,
        val runningTime: String,
        val type: String,
        val description: String,
        val rate: Float?,
        val trailerUrl: String) : Parcelable {

    fun getTrailerUri() = try {
        Uri.parse(trailerUrl)
    } catch (exception: Exception) {
        e(exception, "Video uri parse failed: $this")
        null
    }
}