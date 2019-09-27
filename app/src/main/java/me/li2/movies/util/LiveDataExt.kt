package me.li2.movies.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import me.li2.movies.arch.AppException
import me.li2.movies.arch.Resource

fun <T> MutableLiveData<Resource<T>>.postSuccess(data: T) =
        postValue(Resource.success(data))

fun <T> MutableLiveData<Resource<T>>.postLoading() =
        postValue(Resource.loading(value?.data))

fun <T> MutableLiveData<Resource<T>>.postError(exception: Throwable? = null) =
        postValue(Resource.error(AppException(exception)))

fun <T : Any, L : LiveData<T>> Fragment.observeOnView(liveData: L, body: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer(body))
}
