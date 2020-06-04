/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.util

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import me.li2.android.common.arch.Resource
import me.li2.android.common.logic.orFalse
import me.li2.movies.R

fun doNothing() {
    // do nothing
}

fun reportException(exception: Exception) {
    FirebaseCrashlytics.getInstance().recordException(exception)
}

fun Context.openUrl(url: String) {
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

fun Fragment.navController() = NavHostFragment.findNavController(this)

fun Fragment.navigate(directions: NavDirections) {
    NavHostFragment.findNavController(this).navigate(directions)
}

fun Fragment.navigateSlideInOut(directions: NavDirections) {
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

fun <T> Resource<List<T>>.noData() = this.data?.isNullOrEmpty().orFalse()