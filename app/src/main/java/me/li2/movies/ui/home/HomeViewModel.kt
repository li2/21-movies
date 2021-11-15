/*
 * Created by Weiyi Li on 2020-04-20.
 * https://github.com/li2
 */
package me.li2.movies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.LOADING
import me.li2.android.common.arch.combineLatest
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.data.model.TimeWindow
import me.li2.movies.data.repository.Repository
import me.li2.movies.util.distinctUntilChanged
import me.li2.movies.util.ioWithLiveData
import org.threeten.bp.LocalDate

class HomeViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _trendingMovies = MutableLiveData<Resource<List<MovieItemUI>>>(Resource.loading(emptyList()))
    internal val trendingMovies: LiveData<Resource<List<MovieItemUI>>>
        get() = _trendingMovies.distinctUntilChanged()

    private val _nowPlayingMovies = MutableLiveData<Resource<List<MovieItemUI>>>(Resource.loading(emptyList()))
    internal val nowPlaying: LiveData<Resource<List<MovieItemUI>>>
        get() = _nowPlayingMovies.distinctUntilChanged()

    private val _upcomingMovies = MutableLiveData<Resource<List<MovieItemUI>>>(Resource.loading(emptyList()))
    internal val upcomingMovies: LiveData<Resource<List<MovieItemUI>>>
        get() = _upcomingMovies.distinctUntilChanged()

    private val _popularMovies = MutableLiveData<Resource<List<MovieItemUI>>>(Resource.loading(emptyList()))
    internal val popularMovies: LiveData<Resource<List<MovieItemUI>>>
        get() = _popularMovies.distinctUntilChanged()

    private val _topMovies = MutableLiveData<Resource<List<MovieItemUI>>>(Resource.loading(emptyList()))
    internal val topMovies: LiveData<Resource<List<MovieItemUI>>>
        get() = _topMovies.distinctUntilChanged()

    internal var isLoading: MediatorLiveData<Boolean> =
        combineLatest(trendingMovies, nowPlaying, upcomingMovies, popularMovies, topMovies) { results ->
            (results[0] as Resource<*>).status == LOADING
                    && (results[1] as Resource<*>).status == LOADING
                    && (results[2] as Resource<*>).status == LOADING
                    && (results[3] as Resource<*>).status == LOADING
                    && (results[4] as Resource<*>).status == LOADING
        }

    init {
        getHomeScreenData(true)
    }

    fun getHomeScreenData(forceRefresh: Boolean = false) {
        getTrendingMovies(forceRefresh = forceRefresh)
        getTopMovies(1, forceRefresh)
        getNowPlayingMovies(1, forceRefresh)
        getUpcomingMovies(1, forceRefresh)
        getPopularMovies(1, forceRefresh)
    }

    private fun getTrendingMovies(
        timeWindow: TimeWindow = TimeWindow.DAY,
        forceRefresh: Boolean = false
    ) {
        ioWithLiveData(_trendingMovies, forceRefresh) {
            repository.getTrendingMovies(timeWindow).results
                .filter {
                    it.releaseDate != null && it.releaseDate.isAfter(LocalDate.now().minusYears(1))
                            && it.voteCount > 20
                            && it.voteAverage > 5.0
                }
                .sortedBy { it.popularity }
                .take(MAXIMUM_DISPLAY_MOVIES)
        }
    }

    private fun getNowPlayingMovies(page: Int, forceRefresh: Boolean = false) {
        ioWithLiveData(_nowPlayingMovies, forceRefresh) {
            repository.getNowPlayingMovies(page).results
        }
    }

    private fun getUpcomingMovies(page: Int, forceRefresh: Boolean = false) {
        ioWithLiveData(_upcomingMovies, forceRefresh) {
            repository.getUpcomingMovies(page).results
        }
    }

    private fun getPopularMovies(page: Int, forceRefresh: Boolean = false) {
        ioWithLiveData(_popularMovies, forceRefresh) {
            repository.getPopularMovies(page).results
        }
    }

    private fun getTopMovies(page: Int, forceRefresh: Boolean = false) {
        ioWithLiveData(_topMovies, forceRefresh) {
            repository.getTopMovies(page).results
        }
    }

    companion object {
        private const val MAXIMUM_DISPLAY_MOVIES = 10
    }
}
