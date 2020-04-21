package me.li2.movies.data.remote

import me.li2.movies.App
import me.li2.movies.ui.movies.MoviesType
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class RemoteDataSource : KodeinAware {

    override val kodein by kodein(App.context)
    private val abcApi: AbcApi by instance(arg = TIMEOUT)
    private val tmdbApi: TmdbApi by instance()

    fun getMoviesAsync(type: MoviesType) = abcApi.getMoviesAsync(type.value)

    fun getTopMoviesAsync(page: Int) = tmdbApi.getTopMoviesAsync(page)

    fun getNowPlayingMoviesAsync(page: Int) = tmdbApi.getNowPlayingMoviesAsync(page)

    fun getUpcomingMoviesAsync(page: Int) = tmdbApi.getUpcomingMoviesAsync(page)

    fun getPopularMoviesAsync(page: Int) = tmdbApi.getPopularMoviesAsync(page)

    companion object {
        const val TIMEOUT = 6000L
    }
}
