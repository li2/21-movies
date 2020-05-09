package me.li2.movies.data.repository

import androidx.lifecycle.MutableLiveData
import me.li2.android.common.arch.Resource
import me.li2.movies.App
import me.li2.movies.data.local.LocalDataSource
import me.li2.movies.data.model.MapperUI
import me.li2.movies.data.model.MovieDetailUI
import me.li2.movies.data.model.MovieReviewUI
import me.li2.movies.data.remote.TmdbApi
import me.li2.movies.data.remote.TmdbDataSource
import me.li2.movies.util.RateLimiter
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class Repository : KodeinAware {
    override val kodein by kodein(App.context)
    private val tmdbDataSource by instance<TmdbDataSource>()
    private val localDataSource by instance<LocalDataSource>()
    private val tmdbApiRateLimit: RateLimiter<String> = RateLimiter(2, TimeUnit.MINUTES)

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

    suspend fun getMovieVideos(movieId: Int) = tmdbDataSource.getMovieVideosAsync(movieId).await()

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

    suspend fun getMovieRecommendations(movieId: Int, page: Int) =
            tmdbDataSource.getMovieRecommendationsAsync(movieId, page).await()

    suspend fun searchMovies(keyword: String, page: Int, year: Int? = null) =
            tmdbDataSource.searchMoviesAsync(keyword, page, year).await()
}
