/*
 * Created by Weiyi Li on 2020-05-04.
 * https://github.com/li2
 */
package me.li2.movies.data.local

import me.li2.movies.App
import me.li2.movies.data.model.*
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

    suspend fun saveToWatchlist(movieId: Int): Long {
        return db.watchlistDao().insertMovie(WatchlistMovieDB(movieId))
    }

    fun removeFromWatchlist(movieId: Int) {
        return db.watchlistDao().deleteMovie(movieId)
    }

    suspend fun isMovieInWatchlist(movieId: Int): Boolean {
        return db.watchlistDao().getMovie(movieId) != null
    }

    suspend fun getWatchlist(): List<MovieDetailUI> {
        return db.watchlistDao().getWatchlist()
    }
}