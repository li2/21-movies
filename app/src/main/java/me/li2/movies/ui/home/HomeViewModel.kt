package me.li2.movies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.li2.android.common.arch.Resource
import me.li2.movies.base.BaseViewModel
import me.li2.movies.data.model.MapperUI
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.util.distinctUntilChanged
import me.li2.movies.util.ioWithLiveData

class HomeViewModel : BaseViewModel() {

    private val _topMovies = MutableLiveData<Resource<List<MovieItemUI>?>>()
    internal val topMovies: LiveData<Resource<List<MovieItemUI>?>>
        get() = _topMovies.distinctUntilChanged()

    private val _nowPlayingMovies = MutableLiveData<Resource<List<MovieItemUI>?>>()
    internal val nowPlaying: LiveData<Resource<List<MovieItemUI>?>>
        get() = _nowPlayingMovies.distinctUntilChanged()

    private val _upcomingMovies = MutableLiveData<Resource<List<MovieItemUI>?>>()
    internal val upcomingMovies: LiveData<Resource<List<MovieItemUI>?>>
        get() = _upcomingMovies.distinctUntilChanged()

    private val _popularMovies = MutableLiveData<Resource<List<MovieItemUI>?>>()
    internal val popularMovies: LiveData<Resource<List<MovieItemUI>?>>
        get() = _popularMovies.distinctUntilChanged()

    init {
        getHomeScreenData()
    }

    fun getHomeScreenData(forceRefresh: Boolean = false) {
        getTopMovies(1, forceRefresh)
        getNowPlayingMovies(1, forceRefresh)
        getUpcomingMovies(1, forceRefresh)
        getPopularMovies(1, forceRefresh)
    }

    private fun getTopMovies(page: Int, forceRefresh: Boolean = false) {
        ioWithLiveData(_topMovies, forceRefresh) {
            repository.getTopMovies(page).results.take(MAXIMUM_DISPLAY_MOVIES + 3).takeLast(MAXIMUM_DISPLAY_MOVIES).map {
                MapperUI.toMovieItemUI(it)
            }
        }
    }

    private fun getNowPlayingMovies(page: Int, forceRefresh: Boolean = false) {
        ioWithLiveData(_nowPlayingMovies, forceRefresh) {
            repository.getNowPlayingMovies(page).results.take(MAXIMUM_DISPLAY_MOVIES).map {
                MapperUI.toMovieItemUI(it)
            }
        }
    }

    private fun getUpcomingMovies(page: Int, forceRefresh: Boolean = false) {
        ioWithLiveData(_upcomingMovies, forceRefresh) {
            repository.getUpcomingMovies(page).results.take(MAXIMUM_DISPLAY_MOVIES).map {
                MapperUI.toMovieItemUI(it)
            }
        }
    }

    private fun getPopularMovies(page: Int, forceRefresh: Boolean = false) {
        ioWithLiveData(_popularMovies, forceRefresh) {
            repository.getPopularMovies(page).results.take(MAXIMUM_DISPLAY_MOVIES).map {
                MapperUI.toMovieItemUI(it)
            }
        }
    }

    companion object {
        private const val MAXIMUM_DISPLAY_MOVIES = 10
    }
}
