package me.li2.movies.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetailUI(
        val id: Int,
        val title: String,
        val overview: String,
        val tagline: String,
        val releaseDate: String,
        val runtime: Int,
        val originalLanguage: String,
        val spokenLanguages: List<String>,
        val posterUrl: String?,
        val backdropUrl: String?,
        val popularity: Double,
        val voteAverage: Double,
        val voteCount: Int
) : Parcelable