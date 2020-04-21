package me.li2.movies.ui.movies

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
import me.li2.movies.ui.movies.MoviesType.NOT_SHOWING

class MainViewModel : BaseViewModel() {

    private val notShowingMoviesLiveData: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()
    internal val notShowingMovies: LiveData<Resource<List<MovieItem>>> = notShowingMoviesLiveData

    private val comingSoonMoviesLiveData: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()
    internal val comingSoonMovies: LiveData<Resource<List<MovieItem>>> = comingSoonMoviesLiveData

    fun getMovies(type: MoviesType, forceRefresh: Boolean = false) {
        val moviesLiveData =
                if (type == NOT_SHOWING) notShowingMoviesLiveData else comingSoonMoviesLiveData
        if (!forceRefresh) {
            moviesLiveData.value?.data?.let { movies ->
                moviesLiveData.postSuccess(movies)
                return
            }
        }
        moviesLiveData.postLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                moviesLiveData.postSuccess(repository.getMovies(type))
            } catch (exception: Exception) {
                moviesLiveData.postError(exception)
            }
        }
    }

    fun getUpcomingMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getTopMovies(1).results.take(5).map { MapperUI.toMovieItemUI(it) }
            } catch (exception: Exception) {

            }
        }
    }
}
