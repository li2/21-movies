/*
 * Created by Weiyi Li on 2020-04-19.
 * https://github.com/li2
 */
package me.li2.movies.data.remote

import me.li2.movies.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("trending/movie/{timeWindow}")
    suspend fun getTrendingMovies(@Path("timeWindow") timeWindow: String): TmdbMovieListAPI

    @GET("movie/top_rated")
    suspend fun getTopMovies(@Query("page") page: Int): TmdbMovieListAPI

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("page") page: Int): TmdbMovieListAPI

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("page") page: Int): TmdbMovieListAPI

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): TmdbMovieListAPI

    @GET("movie/{movieId}")
    suspend fun getMovieDetail(@Path("movieId") movieId: Int): TmdbMovieDetailAPI

    @GET("movie/{movieId}/videos")
    suspend fun getMovieVideos(@Path("movieId") movieId: Int): TmdbMovieVideoListAPI

    @GET("movie/{movieId}/credits")
    suspend fun getMovieCredits(@Path("movieId") movieId: Int): TmdbMovieCreditListAPI

    @GET("movie/{movieId}/reviews")
    suspend fun getMovieReviews(@Path("movieId") movieId: Int,
                                @Query("page") page: Int): TmdbMovieReviewListAPI

    @GET("movie/{movieId}/recommendations")
    suspend fun getMovieRecommendations(@Path("movieId") movieId: Int,
                                        @Query("page") page: Int): TmdbMovieListAPI

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") keyword: String,
                             @Query("page") page: Int,
                             @Query("year") year: Int? = null): TmdbMovieListAPI

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresAPI

    companion object {
        const val TIMEOUT = 12L
        const val TMDB_STARTING_PAGE_INDEX = 1

        /**
         * @see <a href="https://developers.themoviedb.org/3/getting-started/images">Images</a>
         */
        fun imageW500Url(path: String?): String? {
            return if (!path.isNullOrEmpty()) "https://image.tmdb.org/t/p/w500/$path" else null
        }

        fun imageOriginalUrl(path: String?): String? {
            return if (!path.isNullOrEmpty()) "https://image.tmdb.org/t/p/original/$path" else null
        }

        fun youtubeTrailerUrl(path: String?): String? {
            return if (!path.isNullOrEmpty()) "https://www.youtube.com/watch?v=$path" else null
        }

        fun youtubeTrailerThumbnailUrl(path: String?): String? {
            return if (!path.isNullOrEmpty()) return "https://img.youtube.com/vi/$path/0.jpg" else null
        }

        fun imdbUrl(imdbId: String?): String? {
            return if (!imdbId.isNullOrEmpty()) "https://www.imdb.com/title/$imdbId" else null
        }
    }
}