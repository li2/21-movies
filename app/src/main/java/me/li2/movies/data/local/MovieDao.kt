/*
 * Created by Weiyi Li on 2020-05-04.
 * https://github.com/li2
 */
package me.li2.movies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.li2.movies.data.local.AppDatabase.Companion.SELECT_FROM_MOVIES
import me.li2.movies.data.model.MovieDetailUI

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieDetail(movieDetail: MovieDetailUI): Long

    @Query("$SELECT_FROM_MOVIES WHERE id = :movieId")
    suspend fun getMovieDetail(movieId: Int): MovieDetailUI?
}