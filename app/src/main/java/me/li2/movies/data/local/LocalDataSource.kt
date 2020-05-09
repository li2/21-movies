package me.li2.movies.data.local

import me.li2.movies.App
import me.li2.movies.data.model.MovieDetailUI
import me.li2.movies.data.model.MovieReviewUI
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LocalDataSource : KodeinAware {
    override val kodein by kodein(App.context)
    private val db by instance<AppDatabase>()

    suspend fun saveMovieDetail(movieDetail: MovieDetailUI): Long =
            db.movieDao().saveMovieDetail(movieDetail)

    suspend fun getMovieDetail(movieId: Int): MovieDetailUI? =
            db.movieDao().getMovieDetail(movieId)

    suspend fun insertReviews(movieId: Int, reviews: List<MovieReviewUI>): List<Long> {
        db.reviewsDao().deleteReviews(movieId)
        return db.reviewsDao().insertReviews(reviews)
    }

    suspend fun getReviews(movieId: Int): List<MovieReviewUI>? =
            db.reviewsDao().getReviews(movieId)
}