package me.li2.movies.data.remote

import me.li2.movies.App
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class TmdbDataSource : KodeinAware {

    override val kodein by kodein(App.context)
    private val tmdbApi: TmdbApi by instance()

    fun getTopMoviesAsync(page: Int) = tmdbApi.getTopMoviesAsync(page)

    fun getNowPlayingMoviesAsync(page: Int) = tmdbApi.getNowPlayingMoviesAsync(page)

    fun getUpcomingMoviesAsync(page: Int) = tmdbApi.getUpcomingMoviesAsync(page)

    fun getPopularMoviesAsync(page: Int) = tmdbApi.getPopularMoviesAsync(page)

    fun getMovieDetailAsync(movieId: Int) = tmdbApi.getMovieDetailAsync(movieId)

    fun getMovieVideosAsync(movieId: Int) = tmdbApi.getMovieVideosAsync(movieId)

    fun getMovieReviewsAsync(movieId: Int, page: Int) = tmdbApi.getMovieReviewsAsync(movieId, page)

    fun getMovieRecommendationsAsync(movieId: Int, page: Int) =
            tmdbApi.getMovieRecommendationsAsync(movieId, page)

    fun searchMoviesAsync(keyword: String, page: Int, year: Int? = null) =
            tmdbApi.searchMoviesAsync(keyword, page, year)
}
