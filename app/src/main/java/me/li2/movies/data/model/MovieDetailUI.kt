/*
 * Created by Weiyi Li on 2020-04-25.
 * https://github.com/li2
 */
package me.li2.movies.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import me.li2.movies.data.local.AppDatabase.Companion.TABLE_MOVIES
import org.threeten.bp.LocalDate

@Parcelize
@Entity(tableName = TABLE_MOVIES,
        indices = [Index(value = ["title", "release_date"], unique = false)])
data class MovieDetailUI(
        @PrimaryKey
        val id: Int,
        val title: String,
        val overview: String,
        val tagline: String,
        val releaseDate: LocalDate?,
        @ColumnInfo(name = "release_date")
        val releaseDateDisplay: String,
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
        val voteAverageDisplay: String,
        val voteCount: Int,
        val voteCountDisplay: String
) : Parcelable {
    fun getDisplayGenres() = genres.joinToString(separator = ", ") { it.name }
}

@Parcelize
@Serializable
data class GenreUI(val id: Int, val name: String) : Parcelable