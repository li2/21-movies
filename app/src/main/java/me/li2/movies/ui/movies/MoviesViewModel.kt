/*
 * Created by Weiyi Li on 2020-05-16.
 * https://github.com/li2
 */
package me.li2.movies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.li2.android.common.arch.*
import me.li2.movies.data.model.MovieItemPagingUI
import me.li2.movies.data.model.isFirstPage
import me.li2.movies.data.model.isLastPage
import me.li2.movies.data.model.nextPage
import me.li2.movies.data.remote.TmdbApi.Companion.TMDB_STARTING_PAGE_INDEX
import me.li2.movies.data.repository.Repository
import me.li2.movies.ui.filter.MoviesFilter
import me.li2.movies.ui.sort.MoviesSort
import me.li2.movies.ui.sort.MoviesSortType
import me.li2.movies.util.distinctUntilChanged
import me.li2.movies.util.doNothing
import me.li2.movies.util.io
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _movies = MutableLiveData<Resource<MovieItemPagingUI>>()
    internal val movies: LiveData<Resource<MovieItemPagingUI>>
        get() = _movies.distinctUntilChanged()

    internal val canLoadMoreMovies: Boolean
        get() = _movies.isIdle() && !_movies.isLastPage()

    internal val isFirstPage: Boolean
        get() = _movies.isFirstPage()

    internal val sort = MoviesSort()
    internal val filter = MoviesFilter()
    private var unfilteredMovies: MovieItemPagingUI? = null

    fun getMovies(category: MoviesCategory, forceRefresh: Boolean = false) {
        if (_movies.isLoading()) {
            doNothing()
            return
        }
        _movies.postLoading()
        io({
            _movies.postError(it)
        }, {
            val pagingUI = getMoviesByCategory(category, forceRefresh)
            if (!forceRefresh) {
                pagingUI.results.addAll(0, unfilteredMovies?.results.orEmpty())
            }
            unfilteredMovies = pagingUI
            filterMovies()
        })
    }

    private suspend fun getMoviesByCategory(
        category: MoviesCategory,
        forceRefresh: Boolean
    ): MovieItemPagingUI {
        val nextPage = if (forceRefresh) TMDB_STARTING_PAGE_INDEX else _movies.nextPage()
        return when (category) {
            is TrendingCategory -> repository.getTrendingMovies(category.timeWindow)
            NowPlayingCategory -> repository.getNowPlayingMovies(nextPage)
            UpcomingCategory -> repository.getUpcomingMovies(nextPage)
            PopularCategory -> repository.getPopularMovies(nextPage)
            TopRatedCategory -> repository.getTopMovies(nextPage)
            is RecommendationCategory -> repository.getMovieRecommendations(category.movieId, nextPage)
            is GenreCategory -> repository.searchMovies(category.genre.name, nextPage)
            is QueryCategory -> repository.searchMovies(category.query, nextPage)
            Watchlist -> repository.getWatchlist()
        }
    }

    fun filterMovies(updateFilter: MoviesFilter.() -> Unit = {}) {
        filter.updateFilter()
        viewModelScope.launch {
            unfilteredMovies?.let { pagingUI ->
                val filteredMovies = filter.performFiltering(pagingUI.results)
                _movies.postSuccess(pagingUI.copy(results = filteredMovies.toMutableList()))
            }
        }
    }

    fun sortMovies(sortType: MoviesSortType, descending: Boolean) {
        sort.sortType = sortType
        sort.descending = descending
        viewModelScope.launch {
            _movies.value?.data?.let { pagingUI ->
                val sortedMovies = sort.performSort(pagingUI.results)
                _movies.postSuccess(pagingUI.copy(results = sortedMovies.toMutableList()))
            }
        }
    }
}