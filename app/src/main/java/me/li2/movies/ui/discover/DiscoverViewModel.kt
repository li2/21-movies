/*
 * Created by Weiyi Li on 2020-06-01.
 * https://github.com/li2
 */
package me.li2.movies.ui.discover

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.li2.android.common.arch.Resource
import me.li2.movies.data.model.GenreUI
import me.li2.movies.data.model.TimeWindow
import me.li2.movies.data.repository.Repository
import me.li2.movies.ui.movies.*

class DiscoverViewModel(
    private val repository: Repository
) : ViewModel() {
    private val _genres = MutableLiveData<Resource<List<GenreUI>>>(Resource.loading(emptyList()))

    internal val genresCategories: LiveData<List<MoviesCategory>>
        get() = Transformations.map(_genres) { resource ->
            resource.data.orEmpty().map { GenreCategory(it) }
        }

    internal val mainCategories = listOf(
        TrendingCategory(TimeWindow.DAY),
        TrendingCategory(TimeWindow.WEEK),
        NowPlayingCategory,
        UpcomingCategory,
        PopularCategory,
        TopRatedCategory
    )

    init {
        getGenres()
    }

    private fun getGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getGenres(_genres)
        }
    }
}