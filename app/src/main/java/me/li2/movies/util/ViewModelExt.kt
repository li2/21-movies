/*
 * Created by Weiyi Li on 2020-04-25.
 * https://github.com/li2
 */
package me.li2.movies.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.postError
import me.li2.android.common.arch.postLoading
import me.li2.android.common.arch.postSuccess
import timber.log.Timber

fun ViewModel.io(onError: (Exception) -> Unit = {},
                 ioHandler: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            ioHandler()
        } catch (exception: Exception) {
            Timber.e(exception)
            onError(exception)
        }
    }
}

fun <T> ViewModel.ioWithLiveData(mutableLiveData: MutableLiveData<Resource<T>>,
                                 forceRefresh: Boolean = false,
                                 ioHandler: suspend CoroutineScope.() -> T) {
    if (!forceRefresh) {
        mutableLiveData.value?.data?.let { data ->
            mutableLiveData.postSuccess(data)
            return
        }
    }
    mutableLiveData.postLoading()
    viewModelScope.launch(Dispatchers.IO) {
        try {
            mutableLiveData.postSuccess(ioHandler())
        } catch (exception: Exception) {
            Timber.e(exception)
            mutableLiveData.postError(exception)
        }
    }
}

fun <T> ViewModel.ui(uiHandler: CoroutineScope.() -> T) {
    viewModelScope.launch(Dispatchers.Main) {
        uiHandler()
    }
}