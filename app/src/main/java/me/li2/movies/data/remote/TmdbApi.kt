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

    @GET("movie/{movieId}/videos")
    fun getMovieVideosAsync(@Path("movieId") movieId: Int): Deferred<TmdbMovieVideoListAPI>

    @GET("movie/{movieId}/reviews")
    fun getMovieReviewsAsync(@Path("movieId") movieId: Int,
                             @Query("page") page: Int): Deferred<TmdbMovieReviewListAPI>

    @GET("movie/{movieId}/recommendations")
    fun getMovieRecommendationsAsync(@Path("movieId") movieId: Int,
                                     @Query("page") page: Int): Deferred<TmdbMovieListAPI>

    companion object {
        /**
         * @see <a href="https://developers.themoviedb.org/3/getting-started/images">Images</a>
         */
        fun imageW500Url(key: String?): String? {
            return if (!key.isNullOrEmpty()) "https://image.tmdb.org/t/p/w500/$key" else null
        }

        fun imageOriginalUrl(key: String?): String? {
            return if (!key.isNullOrEmpty()) "https://image.tmdb.org/t/p/original/$key" else null
        }

        fun youtubeTrailerUrl(key: String?): String? {
            return if (!key.isNullOrEmpty()) "https://www.youtube.com/watch?v=$key" else null
        }

        fun imdbUrl(imdbId: String?): String? {
            return if (!imdbId.isNullOrEmpty()) "https://www.imdb.com/title/$imdbId" else null
        }
    }
}