/*
 * Created by Weiyi Li on 2020-05-16.
 * https://github.com/li2
 */
package me.li2.movies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.li2.android.common.arch.*
import me.li2.movies.base.BaseViewModel
import me.li2.movies.data.model.*
import me.li2.movies.util.distinctUntilChanged
import me.li2.movies.util.doNothing
import me.li2.movies.util.io

class MoviesViewModel : BaseViewModel() {

    private val _movies = MutableLiveData<Resource<MovieItemPagingUI>>()
    internal val movies: LiveData<Resource<MovieItemPagingUI>>
        get() = _movies.distinctUntilChanged()

    internal val canLoadMoreMovies: Boolean
        get() = _movies.isIdle() && !_movies.isLastPage()

    private var unfilteredMovies: MovieItemPagingUI? = null
    internal val filter = MoviesFilter()

    fun searchMovies(genre: String) {
        if (_movies.isLoading()) {
            doNothing()
            return
        }
        _movies.postLoading()
        io({
            _movies.postError(it)
        }, {
            val api = repository.searchMovies(genre, _movies.nextPage())
            val pagingUI = MapperUI.toMovieItemPagingUI(api)
            pagingUI.results.addAll(0, unfilteredMovies?.results.orEmpty())
            unfilteredMovies = pagingUI
            filterMovies()
        })
    }

    fun filterMovies() {
        viewModelScope.launch {
            unfilteredMovies?.let { pagingUI ->
                val filteredMovies = filter.performFiltering(pagingUI.results)
                _movies.postSuccess(pagingUI.copy(results = filteredMovies.toMutableList()))
            }
        }
    }
}