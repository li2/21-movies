/*
 * Created by Weiyi Li on 2020-06-06.
 * https://github.com/li2
 */
package me.li2.movies.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import me.li2.movies.data.local.AppDatabase.Companion.TABLE_WATCHLIST

@Serializable
@Entity(tableName = TABLE_WATCHLIST,
        indices = [Index(value = ["movie_id"], unique = true)])
data class WatchlistMovieDB(
        @PrimaryKey
        @ColumnInfo(name = "movie_id")
        val movieId: Int)