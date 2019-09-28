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
import java.lang.Thread.sleep

class MainViewModel : BaseViewModel() {

    private val notShowingMoviesLiveData: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()
    internal val notShowingMovies: LiveData<Resource<List<MovieItem>>> = notShowingMoviesLiveData

    private val comingSoonMoviesLiveData: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()
    internal val comingSoonMovies: LiveData<Resource<List<MovieItem>>> = comingSoonMoviesLiveData

    fun getMovies(type: MoviesType) {
        if (type == NOT_SHOWING) {
            notShowingMoviesLiveData.postLoading()
        } else {
            comingSoonMoviesLiveData.postLoading()
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sleep(1000)
                val result = repository.getMovies(type)
                if (type == NOT_SHOWING) {
                    notShowingMoviesLiveData.postSuccess(result)
                } else {
                    comingSoonMoviesLiveData.postSuccess(result)
                }
            } catch (exception: Exception) {
                if (type == NOT_SHOWING) {
                    notShowingMoviesLiveData.postError(exception)
                } else {
                    comingSoonMoviesLiveData.postError(exception)
                }
            }
        }
    }
}
