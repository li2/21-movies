package me.li2.movies.data.remote

import me.li2.movies.App
import me.li2.movies.ui.movies.MoviesType
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class TmdbDataSource : KodeinAware {

    override val kodein by kodein(App.context)
    private val abcApi: AbcApi by instance(arg = TIMEOUT)
    private val tmdbApi: TmdbApi by instance()

    fun getMoviesAsync(type: MoviesType) = abcApi.getMoviesAsync(type.value)

    fun getTopMoviesAsync(page: Int) = tmdbApi.getTopMoviesAsync(page)

    fun getNowPlayingMoviesAsync(page: Int) = tmdbApi.getNowPlayingMoviesAsync(page)

    fun getUpcomingMoviesAsync(page: Int) = tmdbApi.getUpcomingMoviesAsync(page)

    fun getPopularMoviesAsync(page: Int) = tmdbApi.getPopularMoviesAsync(page)

    fun getMovieDetailAsync(movieId: Int) = tmdbApi.getMovieDetailAsync(movieId)

    fun getMovieVideosAsync(movieId: Int) = tmdbApi.getMovieVideosAsync(movieId)

    fun getMovieReviewsAsync(movieId: Int, page: Int) = tmdbApi.getMovieReviewsAsync(movieId, page)

    companion object {
        const val TIMEOUT = 6000L
    }
}