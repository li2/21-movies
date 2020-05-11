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
    NavHostFragment.findNavController(this).navigate(directions, getDefaultNavOptionsBuilder().build())
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
 * Sets the value to the result of a function that is called when any of `LiveData`s has data
 * or when they receive updates after that.
 * @see <a href="https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7">LiveData beyond the ViewModel — Reactive patterns using Transformations and MediatorLiveData</a>
 */
fun <R, A, B> LiveData<A>.combine(other: LiveData<B>,
                                  onChange: (A?, B?) -> R): MediatorLiveData<R> {
    val result = MediatorLiveData<R>()

    val mergeF = {
        val source1Value = this.value
        val source2Value = other.value
        result.value = onChange.invoke(source1Value, source2Value)
    }

    result.addSource(this) { mergeF.invoke() }
    result.addSource(other) { mergeF.invoke() }

    return result
}

/**
 * Add multiple sources with the same onChanged observer
 */
fun <T : Any> MediatorLiveData<T>.addSources(sources: List<LiveData<T>>, onChanged: () -> Unit) {
    sources.forEach { source ->
        this.addSource(source) { onChanged() }
    }
}

/**
 * Combine two LiveData
 * @see <a href="https://gist.github.com/y-polek/42afe8fdb1518db471fb27e646526a06">gist/combineLatest</a>
 */
fun <A, B> LiveData<A>.combineLatest(b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        addSource(this@combineLatest) {
            if (it == null && value != null) value = null
            lastA = it
            if (lastA != null && lastB != null) value = lastA!! to lastB!!
        }

        addSource(b) {
            if (it == null && value != null) value = null
            lastB = it
            if (lastA != null && lastB != null) value = lastA!! to lastB!!
        }
    }
}

/**
 * Combine three LiveData
 * @see <a href="https://gist.github.com/y-polek/42afe8fdb1518db471fb27e646526a06">gist/combineLatest</a>
 */
fun <A, B, C> combineLatest(a: LiveData<A>,
                            b: LiveData<B>,
                            c: LiveData<C>): LiveData<Triple<A?, B?, C?>> {

    fun Triple<A?, B?, C?>?.copyWithFirst(first: A?): Triple<A?, B?, C?> {
        if (this@copyWithFirst == null) return Triple<A?, B?, C?>(first, null, null)
        return this@copyWithFirst.copy(first = first)
    }

    fun Triple<A?, B?, C?>?.copyWithSecond(second: B?): Triple<A?, B?, C?> {
        if (this@copyWithSecond == null) return Triple<A?, B?, C?>(null, second, null)
        return this@copyWithSecond.copy(second = second)
    }

    fun Triple<A?, B?, C?>?.copyWithThird(third: C?): Triple<A?, B?, C?> {
        if (this@copyWithThird == null) return Triple<A?, B?, C?>(null, null, third)
        return this@copyWithThird.copy(third = third)
    }

    return MediatorLiveData<Triple<A?, B?, C?>>().apply {
        addSource(a) { value = value.copyWithFirst(it) }
        addSource(b) { value = value.copyWithSecond(it) }
        addSource(c) { value = value.copyWithThird(it) }
    }
}

/**
 * CombinedLiveData is a helper class to combine results from multiple LiveData sources.
 * @param sources Variable number of LiveData arguments.
 * @param combine   Function reference that will be used to combine all LiveData data results.
 * @param R         The type of data returned after combining all LiveData data.
 * Usage:
 * CombinedLiveData<SomeType>(
 *     getLiveData1(),
 *     getLiveData2(),
 *     ... ,
 *     getLiveDataN()
 * ) { datas: List<Any?> ->
 *     // Use datas[0], datas[1], ..., datas[N] to return a SomeType value
 * }
 * @see <a href="https://stackoverflow.com/a/60499661/2722270">Combine multiple LiveData</a>
 */
class CombinedLiveData<R>(vararg sources: LiveData<*>,
                          private val combine: (results: List<Any?>) -> R) : MediatorLiveData<R>() {

    private val results: MutableList<Any?> = MutableList(sources.size) { null }

    init {
        sources.forEachIndexed { index, liveData ->
            super.addSource(liveData) {
                results[index] = it
                value = combine(results)
            }
        }
    }
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

@Suppress("UNCHECKED_CAST")
fun <T> Any.checkedResourceItem(): T? {
    return ((this as? Resource<*>)?.data) as? T
}

inline fun <reified T> Any.checkedResourceListItem(): List<T>? {
    return ((this as? Resource<*>)?.data as? List<*>)?.checkItemsAre()
}
