package me.li2.movies.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import me.li2.movies.data.local.AppDatabase.Companion.TABLE_MOVIES

@Parcelize
@Entity(tableName = TABLE_MOVIES,
        indices = [Index(value = ["title", "release_date"], unique = false)])
data class MovieDetailUI(
        @PrimaryKey
        val id: Int,
        val title: String,
        val overview: String,
        val tagline: String,
        @ColumnInfo(name = "release_date")
        val releaseDate: String,
        val runtime: String,
        val genres: List<GenreUI>,
        val productionCountry: String,
        val originalLanguage: String,
        val spokenLanguages: String,
        val imdbUrl: String?,
        val posterUrl: String?,
        val backdropUrl: String?,
        val youtubeTrailerUrl: String?,
        val popularity: String,
        val voteAverage: String,
        val voteCount: String
) : Parcelable {
    fun getDisplayGenres() = genres.joinToString(separator = ", ") { it.name }
}

@Parcelize
@Serializable
data class GenreUI(val id: Int, val name: String) : Parcelable