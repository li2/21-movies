package me.li2.movies.ui.movies

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class MoviesType : Parcelable {
    NOT_SHOWING,
    COMING_SOON
}

data class MovieItem(
        val id: Long,
        val name: String,
        val runningTime: String,
        val type: String,
        val rate: Float?,
        val trailerUrl: String)