package me.li2.movies.ui.movies

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class MoviesType(val value: String) : Parcelable {
    NOT_SHOWING("notShowing"),
    COMING_SOON("comingSoon")
}

@Parcelize
data class MovieItem(
        val id: Long,
        val name: String,
        val runningTime: String,
        val type: String,
        val description: String,
        val rate: Float?,
        val trailerUri: Uri?) : Parcelable