/*
 * Created by Weiyi Li on 2020-06-01.
 * https://github.com/li2
 */
package me.li2.movies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.li2.movies.data.local.AppDatabase.Companion.SELECT_FROM_GENRES
import me.li2.movies.data.model.GenreUI

@Dao
interface GenresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(trailers: List<GenreUI>): List<Long>

    @Query(SELECT_FROM_GENRES)
    suspend fun getGenres(): List<GenreUI>?
}