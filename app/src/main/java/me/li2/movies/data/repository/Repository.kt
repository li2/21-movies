/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.data.repository

import androidx.lifecycle.MutableLiveData
import me.li2.android.common.arch.RateLimiter
import me.li2.android.common.arch.Resource
import me.li2.movies.App
import me.li2.movies.data.local.LocalDataSource
import me.li2.movies.data.model.*
import me.li2.movies.data.remote.TmdbApi
import me.li2.movies.data.remote.TmdbDataSource
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class Repository : KodeinAware {
    override val kodein by kodein(App.context)
    private val tmdbDataSource by instance<TmdbDataSource>()
    private val localDataSource by instance<LocalDataSource>()
    private val tmdbApiRateLimit: RateLimiter<String> = RateLimiter(2, TimeUnit.MINUTES)

    suspend fun getTrendingMovies(timeWindow: TimeWindow) = tmdbDataSource.getTrendingMoviesAsync(timeWindow).await()

    suspend fun getTopMovies(page: Int) = tmdbDataSource.getTopMoviesAsync(page).await()

    suspend fun getNowPlayingMovies(page: Int) = tmdbDataSource.getNowPlayingMoviesAsync(page).await()

    suspend fun getUpcomingMovies(page: Int) = tmdbDataSource.getUpcomingMoviesAsync(page).await()

    suspend fun getPopularMovies(page: Int) = tmdbDataSource.getPopularMoviesAsync(page).await()

    //suspend fun getMovieDetail(movieId: Int) = tmdbDataSource.getMovieDetailAsync(movieId).await()

    suspend fun getMovieDetail(movieId: Int, result: MutableLiveData<Resource<MovieDetailUI>>) {
        val cacheKey = "getMovieDetail:$movieId"
        object : NetworkBoundResource<MovieDetailUI>(result) {
            override suspend fun loadFromDb(): MovieDetailUI? {
                return localDataSource.getMovieDetail(movieId)
            }

            override fun shouldFetch(data: MovieDetailUI?): Boolean {
                return data == null || tmdbApiRateLimit.shouldFetch(cacheKey)
            }

            override suspend fun fetch(): MovieDetailUI {
                val apiResponse = tmdbDataSource.getMovieDetailAsync(movieId).await()
                return MapperUI.toMovieDetailUI(apiResponse)
            }

            override suspend fun saveFetchResult(data: MovieDetailUI) {
                localDataSource.saveMovieDetail(data)
            }

            override fun onFetchFailed() {
                tmdbApiRateLimit.reset(cacheKey)
            }
        }.load()
    }

    suspend fun getMovieTrailers(movieId: Int, result: MutableLiveData<Resource<List<Trailer>>>) {
        object : NetworkBoundResource<List<Trailer>>(result) {
            override suspend fun loadFromDb(): List<Trailer>? {
                return localDataSource.getTrailers(movieId)
            }

            override fun shouldFetch(data: List<Trailer>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun fetch(): List<Trailer> {
                val apiResponse = tmdbDataSource.getMovieVideosAsync(movieId).await()
                return MapperUI.toTrailersUI(apiResponse)
            }

            override suspend fun saveFetchResult(data: List<Trailer>) {
                localDataSource.insertTrailers(data)
            }
        }.load()
    }

    suspend fun getMovieReviews(movieId: Int,
                                result: MutableLiveData<Resource<List<MovieReviewUI>>>) {
        val cacheKey = "getMovieReviews:$movieId"
        object : NetworkBoundResource<List<MovieReviewUI>>(result) {
            override suspend fun loadFromDb(): List<MovieReviewUI>? {
                return localDataSource.getReviews(movieId)
            }

            override fun shouldFetch(data: List<MovieReviewUI>?): Boolean {
                return data.isNullOrEmpty() || tmdbApiRateLimit.shouldFetch(cacheKey)
            }

            override suspend fun fetch(): List<MovieReviewUI> {
                val apiResponse = tmdbDataSource.getMovieReviewsAsync(movieId, TmdbApi.TMDB_STARTING_PAGE_INDEX).await()
                return MapperUI.toMovieReviewsUI(apiResponse).reviews
            }

            override suspend fun saveFetchResult(data: List<MovieReviewUI>) {
                localDataSource.insertReviews(movieId, data)
            }

            override fun onFetchFailed() {
                tmdbApiRateLimit.reset(cacheKey)
            }
        }.load()
    }

    suspend fun getMovieCredits(movieId: Int) =
            MapperUI.toCreditListUI(tmdbDataSource.getMovieCreditsAsync(movieId).await())

    suspend fun getMovieRecommendations(movieId: Int, page: Int) =
            tmdbDataSource.getMovieRecommendationsAsync(movieId, page).await()

    suspend fun searchMovies(keyword: String, page: Int, year: Int? = null) =
            tmdbDataSource.searchMoviesAsync(keyword, page, year).await()

    suspend fun getGenres(result: MutableLiveData<Resource<List<GenreUI>>>) {
        return object : NetworkBoundResource<List<GenreUI>>(result) {
            override suspend fun loadFromDb() = localDataSource.getGenres()

            override fun shouldFetch(data: List<GenreUI>?) = data.isNullOrEmpty()

            override suspend fun fetch() =
                    tmdbDataSource.getGenresAsync().await().genres.map { MapperUI.toGenreUI(it) }

            override suspend fun saveFetchResult(data: List<GenreUI>) =
                    localDataSource.insertGenres(data)
        }.load()
    }
}
