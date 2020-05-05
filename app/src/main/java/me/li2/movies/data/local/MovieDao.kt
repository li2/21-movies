package me.li2.movies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.li2.movies.data.local.AppDatabase.Companion.SELECT_FROM_MOVIE_DETAIL
import me.li2.movies.data.model.MovieDetailUI

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieDetail(movieDetailUI: MovieDetailUI): Long

    @Query("$SELECT_FROM_MOVIE_DETAIL WHERE id = :movieId")
    suspend fun getMovieDetail(movieId: Int): MovieDetailUI?
}