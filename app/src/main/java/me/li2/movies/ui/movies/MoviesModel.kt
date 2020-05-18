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

class MoviesModel : BaseViewModel() {

    private val _genreMovies = MutableLiveData<Resource<MovieItemPagingUI>>()
    internal val genreMovies: LiveData<Resource<MovieItemPagingUI>>
        get() = _genreMovies.distinctUntilChanged()

    internal val canLoadMoreGenreMovies: Boolean
        get() = _genreMovies.isIdle() && !_genreMovies.isLastPage()

    fun searchGenreMovies(genre: String) {
        if (_genreMovies.isLoading()) {
            return
        }
        _genreMovies.postLoading()
        io({
            _genreMovies.postError(it)
        }, {
            val api = repository.searchMovies(genre, _genreMovies.nextPage())
            val ui = MapperUI.toMovieItemPagingUI(api)
            _genreMovies.postSuccess(ui.copy(results = _genreMovies.appendResults(ui.results)))
        })
    }
}