/*
 * Created by Weiyi Li on 2020-06-06.
 * https://github.com/li2
 */
package me.li2.movies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.li2.movies.data.local.AppDatabase.Companion.DELETE_FROM_WATCHLIST
import me.li2.movies.data.local.AppDatabase.Companion.SELECT_FROM_MOVIES
import me.li2.movies.data.local.AppDatabase.Companion.SELECT_FROM_WATCHLIST
import me.li2.movies.data.local.AppDatabase.Companion.TABLE_MOVIES
import me.li2.movies.data.local.AppDatabase.Companion.TABLE_WATCHLIST
import me.li2.movies.data.model.MovieDetailUI
import me.li2.movies.data.model.WatchlistMovieDB

@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: WatchlistMovieDB): Long

    @Query("$DELETE_FROM_WATCHLIST WHERE movie_id = :movieId")
    fun deleteMovie(movieId: Int)

    @Query("$SELECT_FROM_WATCHLIST WHERE movie_id = :movieId")
    suspend fun getMovie(movieId: Int): WatchlistMovieDB?

    @Query("$SELECT_FROM_MOVIES " +
            "INNER JOIN $TABLE_WATCHLIST ON $TABLE_WATCHLIST.movie_id = $TABLE_MOVIES.id ")
    suspend fun getWatchlist(): List<MovieDetailUI>
}