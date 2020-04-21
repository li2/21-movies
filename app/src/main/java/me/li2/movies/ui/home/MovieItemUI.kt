package me.li2.movies.ui.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieItemUI(
        val id: Int,
        val title: String,
        val releaseDate: String,
        val posterUrl: String?,
        val voteAverage: Double,
        val voteCount: Int,
        val overview: String): Parcelable