package me.li2.movies.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import me.li2.movies.App
import me.li2.movies.data.repository.Repository
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext

open class BaseViewModel :
        ViewModel(),
        LifecycleObserver,
        CoroutineScope,
        KodeinAware {

    final override val kodein by kodein(App.context)

    val repository: Repository by instance()

    private val viewModelJob: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = viewModelJob + Dispatchers.Main

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun clearViewModel() {
        viewModelJob.cancel()
    }

    override fun onCleared() {
        clearViewModel()
        super.onCleared()
    }
}
