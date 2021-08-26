/*
 * Created by Weiyi Li on 2020-04-25.
 * https://github.com/li2
 */
package me.li2.movies.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.postError
import me.li2.android.common.arch.postLoading
import me.li2.android.common.arch.postSuccess
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.data.repository.Repository
import me.li2.movies.util.distinctUntilChanged
import me.li2.movies.util.io

class MovieDetailViewModel2(
    private val movieItem: MovieItemUI,
    private val repository: Repository
) : ViewModel() {

    private val _recommendations = MutableLiveData<Resource<List<MovieItemUI>>>(Resource.loading(emptyList()))
    val recommendations: LiveData<Resource<List<MovieItemUI>>>
        get() = _recommendations.distinctUntilChanged()

    fun getMovieRecommendations() {
        _recommendations.postLoading()
        io({
            _recommendations.postError(it)
        }, {
            val movies = repository.getMovieRecommendations(movieItem.id, 1).results
            _recommendations.postSuccess(movies)
        })
    }
}