package me.li2.movies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.li2.movies.data.local.AppDatabase.Companion.DELETE_FROM_REVIEWS
import me.li2.movies.data.local.AppDatabase.Companion.SELECT_FROM_REVIEWS
import me.li2.movies.data.model.MovieReviewUI

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<MovieReviewUI>): List<Long>

    @Query("$SELECT_FROM_REVIEWS WHERE movie_id = :movieId")
    suspend fun getReviews(movieId: Int): List<MovieReviewUI>?

    @Query("$DELETE_FROM_REVIEWS WHERE movie_id = :movieId")
    fun deleteReviews(movieId: Int)
}