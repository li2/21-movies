package me.li2.movies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.li2.movies.arch.Resource
import me.li2.movies.base.BaseViewModel
import me.li2.movies.ui.movies.MoviesType.NOT_SHOWING
import me.li2.movies.util.postError
import me.li2.movies.util.postLoading
import me.li2.movies.util.postSuccess

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
}
