package me.li2.movies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.postError
import me.li2.android.common.arch.postLoading
import me.li2.android.common.arch.postSuccess
import me.li2.movies.base.BaseViewModel
import me.li2.movies.data.model.MapperUI
import me.li2.movies.ui.home.top.TopItemUI

class HomeViewModel : BaseViewModel() {

    private val topItemsMutableLiveData: MutableLiveData<Resource<List<TopItemUI>>> = MutableLiveData()
    internal val topItemsLiveData: LiveData<Resource<List<TopItemUI>>> = topItemsMutableLiveData

    fun getTopItems(forceRefresh: Boolean = false) {
        if (!forceRefresh) {
            topItemsMutableLiveData.value?.data?.let { items ->
                topItemsMutableLiveData.postSuccess(items)
                return
            }
        }
        topItemsMutableLiveData.postLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val topItems = repository.getUpcomingMovies(1).results
                        .take(5)
                        .map {
                            MapperUI.toTopItemUI(it)
                        }
                topItemsMutableLiveData.postSuccess(topItems)
            } catch (exception: Exception) {
                topItemsMutableLiveData.postError(exception)
            }
        }
    }
}
