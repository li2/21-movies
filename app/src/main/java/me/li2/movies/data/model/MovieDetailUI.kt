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
        val runtime: String,
        val genres: List<GenreUI>,
        val productionCountry: String,
        val originalLanguage: String,
        val spokenLanguages: String,
        val imdbUrl: String?,
        val posterUrl: String?,
        val backdropUrl: String?,
        val popularity: Double,
        val voteAverage: Double,
        val voteCount: Int
) : Parcelable {
    fun getDisplayGenres() = genres.joinToString(separator = ", ") { it.name }
}

@Parcelize
data class GenreUI(val id: Int, val name: String) : Parcelable