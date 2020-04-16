package me.li2.movies.util

import android.os.Build
import android.view.View
import android.view.View.GONE
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import me.li2.android.common.logic.orFalse
import me.li2.movies.R
import org.jetbrains.anko.doFromSdk

fun Fragment.navController() = NavHostFragment.findNavController(this)

fun Fragment.isTablet() = context?.resources?.getBoolean(R.bool.isTablet).orFalse()

fun View.hide() { visibility = GONE }

fun ifSupportLollipop(executor: () -> Unit) {
    doFromSdk(Build.VERSION_CODES.LOLLIPOP) {
        executor()
    }
}
