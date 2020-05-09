package me.li2.movies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.li2.movies.data.local.AppDatabase.Companion.SELECT_FROM_TRAILERS
import me.li2.movies.data.model.Trailer

@Dao
interface TrailerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrailer(trailer: Trailer): Long

    @Query("$SELECT_FROM_TRAILERS WHERE movie_id = :movieId")
    suspend fun getTrailer(movieId: Int): Trailer?
}