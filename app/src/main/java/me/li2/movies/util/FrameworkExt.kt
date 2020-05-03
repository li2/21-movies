package me.li2.movies.util

import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import me.li2.movies.R

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