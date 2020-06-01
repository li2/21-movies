/*
 * Created by Weiyi Li on 2020-05-04.
 * https://github.com/li2
 */
package me.li2.movies.data.local

import me.li2.movies.App
import me.li2.movies.data.model.GenreUI
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

    suspend fun insertTrailers(trailers: List<Trailer>): List<Long> {
        return db.trailerDao().insertTrailers(trailers)
    }

    suspend fun getTrailers(movieId: Int): List<Trailer>? {
        return db.trailerDao().getTrailers(movieId)
    }

    suspend fun insertGenres(genres: List<GenreUI>): List<Long> {
        return db.genresDao().insertGenres(genres)
    }

    suspend fun getGenres(): List<GenreUI>? {
        return db.genresDao().getGenres()
    }
}