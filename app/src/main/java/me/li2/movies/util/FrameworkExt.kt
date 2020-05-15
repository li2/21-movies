package me.li2.movies.util

import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.*
import me.li2.movies.R

fun doNothing() {
    // do nothing
}

fun reportException(exception: Exception) {
    FirebaseCrashlytics.getInstance().recordException(exception)
}

fun Context.watchYoutubeVideo(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}

/*
    app:enterAnim="@anim/slide_in_right"
    app:exitAnim="@anim/slide_out_left"
    app:popEnterAnim="@anim/slide_in_left"
    app:popExitAnim="@anim/slide_out_right"
 */
private fun getDefaultNavOptionsBuilder(): NavOptions.Builder {
    return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
}

fun Fragment.navigate(@NonNull directions: NavDirections) {
    NavHostFragment.findNavController(this).navigate(directions)
}

fun Fragment.setToolbarTitle(title: String) {
    (activity as AppCompatActivity).supportActionBar?.title = title
}

/**
 * Creates a new [LiveData] object does not emit a value until the source `this` LiveData value
 * has been changed.  The value is considered changed if `equals()` yields `false`.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <X> LiveData<X>.distinctUntilChanged(): LiveData<X> =
        Transformations.distinctUntilChanged(this)

/**
 * Sets the value to the result of a function that is called when both `LiveData`s have data
 * or when they receive updates after that.
 *
 * @return mediatorLiveData which must has an active observer attached
 * @see <a href="https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7">LiveData beyond the ViewModel — Reactive patterns using Transformations and MediatorLiveData</a>
 */
fun <T, A, B> LiveData<A>.combine(other: LiveData<B>, onChange: (A, B) -> T): MediatorLiveData<T> {
    var source1emitted = false
    var source2emitted = false

    val result = MediatorLiveData<T>()

    val mergeF = {
        val source1Value = this.value
        val source2Value = other.value

        if (source1emitted && source2emitted) {
            result.value = onChange.invoke(source1Value!!, source2Value!!)
        }
    }

    result.addSource(this) { source1emitted = true; mergeF.invoke() }
    result.addSource(other) { source2emitted = true; mergeF.invoke() }

    return result
}

/**
 * Combine the latest value emitted by each LiveData.
 *
 * CombineLatest emits an item whenever any of the source Observables emits an item
 * (so long as each of the source Observables has emitted at least one item).
 */
fun <R> combineLatest(vararg sources: LiveData<*>,
                      onChange: (results: List<Any>) -> R): MediatorLiveData<R> {
    val sourcesEmitted = sources.map { false }.toMutableList()
    val result = MediatorLiveData<R>()

    val mergeF = {
        if (!sourcesEmitted.contains(false)) {
            // all sources have emitted value
            result.value = onChange.invoke(sources.map { it.value!! })
        }
    }

    sources.forEachIndexed { index, liveData ->
        result.addSource(liveData) {
            sourcesEmitted[index] = true
            mergeF.invoke()
        }
    }

    return result
}

/**
 * Resolve kotlin unchecked cast Any? to List<> warning.
 * @see <a href="https://stackoverflow.com/a/36570969/2722270">Unchecked Cast Any? to List<></a>
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> List<*>.checkItemsAre(): List<T>? {
    return if (all { it is T })
        this as List<T>
    else
        null
}

fun <T> Resource<T>.copy(data: T): Resource<T> {
    return when (status) {
        LOADING -> Resource.loading(data)
        SUCCESS -> Resource.success(data)
        ERROR -> Resource.error(exception, data)
    }
}
