package me.li2.movies.data.repository

import me.li2.movies.App
import me.li2.movies.data.local.DBDataSource
import me.li2.movies.data.model.MapperUI
import me.li2.movies.data.model.TmdbMoviesListAPI
import me.li2.movies.data.remote.RemoteDataSource
import me.li2.movies.ui.movies.MovieItem
import me.li2.movies.ui.movies.MoviesType
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class Repository : KodeinAware {
    override val kodein by kodein(App.context)
    private val remoteDataSource by instance<RemoteDataSource>()
    private val dbDataSource by instance<DBDataSource>()

    suspend fun getMovies(type: MoviesType): List<MovieItem> =
            remoteDataSource.getMoviesAsync(type).await()
                    .map { MapperUI.toMovieUI(it) }

    suspend fun getTopMovies(page: Int): TmdbMoviesListAPI =
            remoteDataSource.getTopMoviesAsync(page).await()

    suspend fun getNowPlayingMoviesAsync(page: Int): TmdbMoviesListAPI =
            remoteDataSource.getNowPlayingMoviesAsync(page).await()

    suspend fun getUpcomingMoviesAsync(page: Int): TmdbMoviesListAPI =
            remoteDataSource.getUpcomingMoviesAsync(page).await()

    suspend fun getPopularMoviesAsync(page: Int): TmdbMoviesListAPI =
            remoteDataSource.getPopularMoviesAsync(page).await()
}
