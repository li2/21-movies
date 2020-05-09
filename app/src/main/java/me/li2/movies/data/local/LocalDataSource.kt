package me.li2.movies.data.local

import me.li2.movies.App
import me.li2.movies.data.model.MovieDetailUI
import me.li2.movies.data.model.MovieReviewUI
import me.li2.movies.data.model.Trailer
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LocalDataSource : KodeinAware {
    override val kodein by kodein(App.context)
    private val db by instance<AppDatabase>()

    suspend fun saveMovieDetail(movieDetail: MovieDetailUI): Long {
        return db.movieDao().saveMovieDetail(movieDetail)
    }

    suspend fun getMovieDetail(movieId: Int): MovieDetailUI? {
        return db.movieDao().getMovieDetail(movieId)
    }

    suspend fun insertReviews(movieId: Int, reviews: List<MovieReviewUI>): List<Long> {
        db.reviewsDao().deleteReviews(movieId)
        return db.reviewsDao().insertReviews(reviews)
    }

    suspend fun getReviews(movieId: Int): List<MovieReviewUI>? {
        return db.reviewsDao().getReviews(movieId)
    }

    suspend fun insertTrailer(trailer: Trailer): Long {
        return db.trailerDao().insertTrailer(trailer)
    }

    suspend fun getTrailer(movieId: Int): Trailer? {
        return db.trailerDao().getTrailer(movieId)
    }
}