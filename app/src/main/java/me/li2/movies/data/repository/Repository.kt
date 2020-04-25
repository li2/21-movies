package me.li2.movies.data.repository

import me.li2.movies.App
import me.li2.movies.data.local.DBDataSource
import me.li2.movies.data.model.MapperUI
import me.li2.movies.data.remote.TmdbDataSource
import me.li2.movies.ui.movies.MovieItem
import me.li2.movies.ui.movies.MoviesType
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class Repository : KodeinAware {
    override val kodein by kodein(App.context)
    private val tmdbDataSource by instance<TmdbDataSource>()
    private val dbDataSource by instance<DBDataSource>()

    suspend fun getMovies(type: MoviesType): List<MovieItem> = tmdbDataSource.getMoviesAsync(type).await()
            .map { MapperUI.toMovieUI(it) }

    suspend fun getTopMovies(page: Int) = tmdbDataSource.getTopMoviesAsync(page).await()

    suspend fun getNowPlayingMoviesAsync(page: Int) = tmdbDataSource.getNowPlayingMoviesAsync(page).await()

    suspend fun getUpcomingMoviesAsync(page: Int) = tmdbDataSource.getUpcomingMoviesAsync(page).await()

    suspend fun getPopularMoviesAsync(page: Int) = tmdbDataSource.getPopularMoviesAsync(page).await()

    suspend fun getMovieDetailAsync(movieId: Int) = tmdbDataSource.getMovieDetailAsync(movieId).await()

    suspend fun getMovieVideosAsync(movieId: Int) = tmdbDataSource.getMovieVideosAsync(movieId).await()

    suspend fun getMovieReviewsAsync(movieId: Int, page: Int) = tmdbDataSource.getMovieReviewsAsync(movieId, page).await()
}
