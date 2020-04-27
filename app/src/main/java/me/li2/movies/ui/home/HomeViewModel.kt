package me.li2.movies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.li2.android.common.arch.Resource
import me.li2.movies.base.BaseViewModel
import me.li2.movies.data.model.MapperUI
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.util.ioWithLiveData

class HomeViewModel : BaseViewModel() {

    private val topMoviesMutableLiveData: MutableLiveData<Resource<List<MovieItemUI>?>> = MutableLiveData()
    internal val topMoviesLiveData: LiveData<Resource<List<MovieItemUI>?>> = topMoviesMutableLiveData

    private val nowPlayingMoviesMutableLiveData: MutableLiveData<Resource<List<MovieItemUI>?>> = MutableLiveData()
    internal val nowPlayingMoviesLiveData: LiveData<Resource<List<MovieItemUI>?>> = nowPlayingMoviesMutableLiveData

    private val upcomingMoviesMutableLiveData: MutableLiveData<Resource<List<MovieItemUI>?>> = MutableLiveData()
    internal val upcomingMoviesLiveData: LiveData<Resource<List<MovieItemUI>?>> = upcomingMoviesMutableLiveData

    private val popularMoviesMutableLiveData: MutableLiveData<Resource<List<MovieItemUI>?>> = MutableLiveData()
    internal val popularMoviesLiveData: LiveData<Resource<List<MovieItemUI>?>> = popularMoviesMutableLiveData

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
        ioWithLiveData(topMoviesMutableLiveData, forceRefresh) {
            repository.getTopMovies(page).results.take(MAXIMUM_DISPLAY_MOVIES).map {
                MapperUI.toMovieItemUI(it)
            }
        }
    }

    private fun getNowPlayingMovies(page: Int, forceRefresh: Boolean = false) {
        ioWithLiveData(nowPlayingMoviesMutableLiveData, forceRefresh) {
            repository.getNowPlayingMovies(page).results.take(MAXIMUM_DISPLAY_MOVIES).map {
                MapperUI.toMovieItemUI(it)
            }
        }
    }

    private fun getUpcomingMovies(page: Int, forceRefresh: Boolean = false) {
        ioWithLiveData(upcomingMoviesMutableLiveData, forceRefresh) {
            repository.getUpcomingMovies(page).results.take(MAXIMUM_DISPLAY_MOVIES).map {
                MapperUI.toMovieItemUI(it)
            }
        }
    }

    private fun getPopularMovies(page: Int, forceRefresh: Boolean = false) {
        ioWithLiveData(popularMoviesMutableLiveData, forceRefresh) {
            repository.getPopularMovies(page).results.take(MAXIMUM_DISPLAY_MOVIES).map {
                MapperUI.toMovieItemUI(it)
            }
        }
    }

    companion object {
        private const val MAXIMUM_DISPLAY_MOVIES = 10
    }
}
