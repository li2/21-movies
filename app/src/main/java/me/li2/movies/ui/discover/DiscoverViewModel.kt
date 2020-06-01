/*
 * Created by Weiyi Li on 2020-06-01.
 * https://github.com/li2
 */
package me.li2.movies.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.li2.android.common.arch.Resource
import me.li2.movies.base.BaseViewModel
import me.li2.movies.data.model.GenreUI
import me.li2.movies.util.distinctUntilChanged

class DiscoverViewModel : BaseViewModel() {

    private val _genres = MutableLiveData<Resource<List<GenreUI>>>(Resource.loading(emptyList()))
    internal val genres: LiveData<Resource<List<GenreUI>>>
        get() = _genres.distinctUntilChanged()

    init {
        getGenres()
    }

    private fun getGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getGenres(_genres)
        }
    }
}