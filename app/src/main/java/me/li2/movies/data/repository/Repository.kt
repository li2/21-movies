/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.data.repository

import androidx.lifecycle.MutableLiveData
import me.li2.android.common.arch.RateLimiter
import me.li2.android.common.arch.Resource
import me.li2.movies.data.local.LocalDataSource
import me.li2.movies.data.model.*
import me.li2.movies.data.remote.TmdbApi
import me.li2.movies.data.remote.TmdbDataSource
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val tmdbDataSource: TmdbDataSource
) {
    private val tmdbApiRateLimit: RateLimiter<String> = RateLimiter(2, TimeUnit.MINUTES)

    suspend fun getTrendingMovies(timeWindow: TimeWindow): MovieItemPagingUI {
        val api = tmdbDataSource.getTrendingMovies(timeWindow)
        return MapperUI.toMovieItemPagingUI(api)
    }

    suspend fun getTopMovies(page: Int): MovieItemPagingUI {
        val api = tmdbDataSource.getTopMovies(page)
        return MapperUI.toMovieItemPagingUI(api)
    }

    suspend fun getNowPlayingMovies(page: Int): MovieItemPagingUI {
        val api = tmdbDataSource.getNowPlayingMovies(page)
        return MapperUI.toMovieItemPagingUI(api)
    }

    suspend fun getUpcomingMovies(page: Int): MovieItemPagingUI {
        val api = tmdbDataSource.getUpcomingMovies(page)
        return MapperUI.toMovieItemPagingUI(api)
    }

    suspend fun getPopularMovies(page: Int): MovieItemPagingUI {
        val api = tmdbDataSource.getPopularMovies(page)
        return MapperUI.toMovieItemPagingUI(api)
    }

    //suspend fun getMovieDetail(movieId: Int) = tmdbDataSource.getMovieDetailAsync(movieId)

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
                val apiResponse = tmdbDataSource.getMovieDetail(movieId)
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
                val apiResponse = tmdbDataSource.getMovieVideos(movieId)
                return MapperUI.toTrailersUI(apiResponse)
            }

            override suspend fun saveFetchResult(data: List<Trailer>) {
                localDataSource.insertTrailers(data)
            }
        }.load()
    }

    suspend fun getMovieReviews(
        movieId: Int,
        result: MutableLiveData<Resource<List<MovieReviewUI>>>
    ) {
        val cacheKey = "getMovieReviews:$movieId"
        object : NetworkBoundResource<List<MovieReviewUI>>(result) {
            override suspend fun loadFromDb(): List<MovieReviewUI>? {
                return localDataSource.getReviews(movieId)
            }

            override fun shouldFetch(data: List<MovieReviewUI>?): Boolean {
                return data.isNullOrEmpty() || tmdbApiRateLimit.shouldFetch(cacheKey)
            }

            override suspend fun fetch(): List<MovieReviewUI> {
                val apiResponse = tmdbDataSource.getMovieReviews(movieId, TmdbApi.TMDB_STARTING_PAGE_INDEX)
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
        MapperUI.toCreditListUI(tmdbDataSource.getMovieCredits(movieId))

    suspend fun getMovieRecommendations(movieId: Int, page: Int): MovieItemPagingUI {
        val api = tmdbDataSource.getMovieRecommendations(movieId, page)
        return MapperUI.toMovieItemPagingUI(api)
    }

    suspend fun searchMovies(keyword: String, page: Int, year: Int? = null): MovieItemPagingUI {
        val api = tmdbDataSource.searchMovies(keyword, page, year)
        return MapperUI.toMovieItemPagingUI(api)
    }

    suspend fun getGenres(result: MutableLiveData<Resource<List<GenreUI>>>) {
        return object : NetworkBoundResource<List<GenreUI>>(result) {
            override suspend fun loadFromDb() = localDataSource.getGenres()

            override fun shouldFetch(data: List<GenreUI>?) = data.isNullOrEmpty()

            override suspend fun fetch() =
                tmdbDataSource.getGenres().genres.map { MapperUI.toGenreUI(it) }

            override suspend fun saveFetchResult(data: List<GenreUI>) =
                localDataSource.insertGenres(data)
        }.load()
    }

    suspend fun saveToWatchlist(movieId: Int): Long = localDataSource.saveToWatchlist(movieId)

    fun removeFromWatchlist(movieId: Int) = localDataSource.removeFromWatchlist(movieId)

    suspend fun isMovieInWatchlist(movieId: Int) = localDataSource.isMovieInWatchlist(movieId)

    suspend fun getWatchlist(): MovieItemPagingUI {
        val watchlist = localDataSource.getWatchlist()
        return MapperUI.toMovieItemPagingUI(watchlist)
    }
}
