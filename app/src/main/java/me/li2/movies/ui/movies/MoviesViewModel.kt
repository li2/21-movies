/*
 * Created by Weiyi Li on 2020-05-16.
 * https://github.com/li2
 */
package me.li2.movies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.li2.android.common.arch.*
import me.li2.movies.base.BaseViewModel
import me.li2.movies.data.model.*
import me.li2.movies.util.distinctUntilChanged
import me.li2.movies.util.io

class MoviesViewModel : BaseViewModel() {

    private val _movies = MutableLiveData<Resource<MovieItemPagingUI>>()
    internal val movies: LiveData<Resource<MovieItemPagingUI>>
        get() = _movies.distinctUntilChanged()

    internal val canLoadMoreMovies: Boolean
        get() = _movies.isIdle() && !_movies.isLastPage()

    fun searchMovies(genre: String) {
        if (_movies.isLoading()) {
            return
        }
        _movies.postLoading()
        io({
            _movies.postError(it)
        }, {
            val api = repository.searchMovies(genre, _movies.nextPage())
            val ui = MapperUI.toMovieItemPagingUI(api)
            _movies.postSuccess(ui.copy(results = _movies.appendResults(ui.results)))
        })
    }
}