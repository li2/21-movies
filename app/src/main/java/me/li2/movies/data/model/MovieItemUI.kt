package me.li2.movies.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieItemUI(
        val id: Int,
        val title: String,
        val releaseDate: String,
        val posterUrl: String?,
        val backdropUrl: String?,
        val voteAverage: String,
        val voteCount: String,
        val overview: String): Parcelable