package me.li2.movies.data.remote

import kotlinx.coroutines.Deferred
import me.li2.movies.data.model.TmdbMovieDetailAPI
import me.li2.movies.data.model.TmdbMovieListAPI
import me.li2.movies.data.model.TmdbMovieReviewListAPI
import me.li2.movies.data.model.TmdbMovieVideoListAPI
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/top_rated")
    fun getTopMoviesAsync(@Query("page") page: Int): Deferred<TmdbMovieListAPI>

    @GET("movie/now_playing")
    fun getNowPlayingMoviesAsync(@Query("page") page: Int): Deferred<TmdbMovieListAPI>

    @GET("movie/upcoming")
    fun getUpcomingMoviesAsync(@Query("page") page: Int): Deferred<TmdbMovieListAPI>

    @GET("movie/popular")
    fun getPopularMoviesAsync(@Query("page") page: Int): Deferred<TmdbMovieListAPI>

    @GET("movie/{movieId}")
    fun getMovieDetailAsync(@Path("movieId") movieId: Int): Deferred<TmdbMovieDetailAPI>

    @GET("/movie/{movie_id}/videos")
    fun getMovieVideosAsync(@Path("movieId") movieId: Int): Deferred<TmdbMovieVideoListAPI>

    @GET("/movie/{movie_id}/reviews")
    fun getMovieReviewsAsync(@Path("movieId") movieId: Int,
                             @Query("page") page: Int): Deferred<TmdbMovieReviewListAPI>

    companion object {
        /**
         * @see <a href="https://developers.themoviedb.org/3/getting-started/images">Images</a>
         */
        fun imageUrl(key: String?): String? {
            return if (!key.isNullOrEmpty()) "https://image.tmdb.org/t/p/w500/$key" else null
        }

        fun youtubeTrailerUrl(key: String?): String? {
            return if (!key.isNullOrEmpty()) "https://www.youtube.com/watch?v=$key" else null
        }
    }
}