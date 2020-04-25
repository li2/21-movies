package me.li2.movies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.postError
import me.li2.android.common.arch.postLoading
import me.li2.android.common.arch.postSuccess
import me.li2.movies.base.BaseViewModel
import me.li2.movies.data.model.MapperUI
import me.li2.movies.data.model.TmdbMovieListAPI

class HomeViewModel : BaseViewModel() {

    private val topMoviesMutableLiveData: MutableLiveData<Resource<List<MovieItemUI>>> = MutableLiveData()
    internal val topMoviesLiveData: LiveData<Resource<List<MovieItemUI>>> = topMoviesMutableLiveData

    private val nowPlayingMoviesMutableLiveData: MutableLiveData<Resource<List<MovieItemUI>>> = MutableLiveData()
    internal val nowPlayingMoviesLiveData: LiveData<Resource<List<MovieItemUI>>> = nowPlayingMoviesMutableLiveData

    private val upcomingMoviesMutableLiveData: MutableLiveData<Resource<List<MovieItemUI>>> = MutableLiveData()
    internal val upcomingMoviesLiveData: LiveData<Resource<List<MovieItemUI>>> = upcomingMoviesMutableLiveData

    private val popularMoviesMutableLiveData: MutableLiveData<Resource<List<MovieItemUI>>> = MutableLiveData()
    internal val popularMoviesLiveData: LiveData<Resource<List<MovieItemUI>>> = popularMoviesMutableLiveData

    private fun getMovies(mutableLiveData: MutableLiveData<Resource<List<MovieItemUI>>>,
                          forceRefresh: Boolean = false,
                          api: suspend () -> TmdbMovieListAPI) {
        if (!forceRefresh) {
            mutableLiveData.value?.data?.let { items ->
                mutableLiveData.postSuccess(items)
                return
            }
        }
        mutableLiveData.postLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = api().results.take(10).map { MapperUI.toMovieItemUI(it) }
                mutableLiveData.postSuccess(movies)
            } catch (exception: Exception) {
                mutableLiveData.postError(exception)
            }
        }
    }

    fun getHomeData(forceRefresh: Boolean = false) {
        getMovies(topMoviesMutableLiveData, forceRefresh) {
            repository.getTopMovies(1)
        }

        getMovies(nowPlayingMoviesMutableLiveData, forceRefresh) {
            repository.getNowPlayingMoviesAsync(1)
        }

        getMovies(upcomingMoviesMutableLiveData, forceRefresh) {
            repository.getUpcomingMoviesAsync(1)
        }

        getMovies(popularMoviesMutableLiveData, forceRefresh) {
            repository.getPopularMoviesAsync(1)
        }
    }
}
