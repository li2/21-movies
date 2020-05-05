package me.li2.movies.data.repository

import me.li2.movies.App
import me.li2.movies.data.local.LocalDataSource
import me.li2.movies.data.remote.TmdbDataSource
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class Repository : KodeinAware {
    override val kodein by kodein(App.context)
    private val tmdbDataSource by instance<TmdbDataSource>()
    val localDataSource by instance<LocalDataSource>()

    suspend fun getTopMovies(page: Int) = tmdbDataSource.getTopMoviesAsync(page).await()

    suspend fun getNowPlayingMovies(page: Int) = tmdbDataSource.getNowPlayingMoviesAsync(page).await()

    suspend fun getUpcomingMovies(page: Int) = tmdbDataSource.getUpcomingMoviesAsync(page).await()

    suspend fun getPopularMovies(page: Int) = tmdbDataSource.getPopularMoviesAsync(page).await()

    suspend fun getMovieDetail(movieId: Int) = tmdbDataSource.getMovieDetailAsync(movieId).await()

    suspend fun getMovieVideos(movieId: Int) = tmdbDataSource.getMovieVideosAsync(movieId).await()

    suspend fun getMovieReviews(movieId: Int, page: Int) =
            tmdbDataSource.getMovieReviewsAsync(movieId, page).await()

    suspend fun getMovieRecommendations(movieId: Int, page: Int) =
            tmdbDataSource.getMovieRecommendationsAsync(movieId, page).await()

    suspend fun searchMovies(keyword: String, page: Int, year: Int? = null) =
            tmdbDataSource.searchMoviesAsync(keyword, page, year).await()
}
