/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.data.remote

import me.li2.movies.data.model.TimeWindow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbDataSource @Inject constructor(
    private val tmdbApi: TmdbApi
) {
    suspend fun getTrendingMovies(timeWindow: TimeWindow) = tmdbApi.getTrendingMovies(timeWindow.value)

    suspend fun getTopMovies(page: Int) = tmdbApi.getTopMovies(page)

    suspend fun getNowPlayingMovies(page: Int) = tmdbApi.getNowPlayingMovies(page)

    suspend fun getUpcomingMovies(page: Int) = tmdbApi.getUpcomingMovies(page)

    suspend fun getPopularMovies(page: Int) = tmdbApi.getPopularMovies(page)

    suspend fun getMovieDetail(movieId: Int) = tmdbApi.getMovieDetail(movieId)

    suspend fun getMovieVideos(movieId: Int) = tmdbApi.getMovieVideos(movieId)

    suspend fun getMovieCredits(movieId: Int) =
        tmdbApi.getMovieCredits(movieId)

    suspend fun getMovieReviews(movieId: Int, page: Int) = tmdbApi.getMovieReviews(movieId, page)

    suspend fun getMovieRecommendations(movieId: Int, page: Int) =
        tmdbApi.getMovieRecommendations(movieId, page)

    suspend fun searchMovies(keyword: String, page: Int, year: Int? = null) =
        tmdbApi.searchMovies(keyword, page, year)

    suspend fun getGenres() = tmdbApi.getGenres()
}
