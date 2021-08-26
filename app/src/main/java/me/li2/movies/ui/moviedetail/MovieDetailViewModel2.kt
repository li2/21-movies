/*
 * Created by Weiyi Li on 2020-04-25.
 * https://github.com/li2
 */
package me.li2.movies.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.postError
import me.li2.android.common.arch.postLoading
import me.li2.android.common.arch.postSuccess
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.data.repository.Repository
import me.li2.movies.util.CoroutineContextProvider

class MovieDetailViewModel2(
    private val movieItem: MovieItemUI,
    private val repository: Repository,
    private val coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {

    private val _recommendations = MutableLiveData<Resource<List<MovieItemUI>>>()
    val recommendations: LiveData<Resource<List<MovieItemUI>>>
        get() = _recommendations

    fun getMovieRecommendations() {
        _recommendations.postLoading()

        viewModelScope.launch(coroutineContextProvider.IO) {
            try {
                val movies = repository.getMovieRecommendations(movieItem.id, 1).results
                _recommendations.postSuccess(movies)
            } catch (exception: Exception) {
                _recommendations.postError(exception)
            }
        }
    }
}