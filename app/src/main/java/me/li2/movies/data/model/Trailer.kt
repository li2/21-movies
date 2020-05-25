/*
 * Created by Weiyi Li on 2020-05-10.
 * https://github.com/li2
 */
package me.li2.movies.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import me.li2.movies.data.local.AppDatabase.Companion.TABLE_TRAILERS

@Serializable
@Entity(tableName = TABLE_TRAILERS,
        indices = [Index(value = ["movie_id"], unique = false)])
data class Trailer(
        @PrimaryKey
        val id: String,
        @ColumnInfo(name = "movie_id")
        val movieId: Int,
        val url: String,
        val thumbnailUrl: String)