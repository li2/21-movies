package me.li2.movies.util

import android.os.Build
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import org.jetbrains.anko.doFromSdk

fun Fragment.navController() = NavHostFragment.findNavController(this)

fun ifSupportLollipop(executor: () -> Unit) {
    doFromSdk(Build.VERSION_CODES.LOLLIPOP) {
        executor()
    }
}
