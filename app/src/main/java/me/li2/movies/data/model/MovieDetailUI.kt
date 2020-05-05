package me.li2.movies.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import me.li2.movies.data.local.AppDatabase.Companion.TABLE_MOVIE_DETAIL

@Parcelize
@Entity(tableName = TABLE_MOVIE_DETAIL,
        indices = [(Index(value = arrayOf("id"), unique = true))])
data class MovieDetailUI(
        @PrimaryKey
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
@Serializable
data class GenreUI(val id: Int, val name: String) : Parcelable