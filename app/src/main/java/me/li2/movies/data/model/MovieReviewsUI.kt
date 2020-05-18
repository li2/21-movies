/*
 * Created by Weiyi Li on 2020-04-26.
 * https://github.com/li2
 */
package me.li2.movies.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import me.li2.movies.data.local.AppDatabase.Companion.TABLE_REVIEWS

@Parcelize
data class MovieReviewListUI(
        val reviews: List<MovieReviewUI>,
        val page: Int,
        val totalPages: Int,
        val totalResults: Int
) : Parcelable

@Parcelize
@Entity(tableName = TABLE_REVIEWS,
        indices = [Index(value = ["movie_id"], unique = false)])
data class MovieReviewUI(
        @PrimaryKey
        val id: String,
        @ColumnInfo(name = "movie_id")
        val movieId: Int,
        val author: String,
        val content: String
) : Parcelable