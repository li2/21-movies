package me.li2.movies.util

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import org.jetbrains.anko.doFromSdk

fun Fragment.navController() = NavHostFragment.findNavController(this)

fun Fragment.setToolbarTitle(title: String) {
    (activity as AppCompatActivity).supportActionBar?.title = title
}

fun ifSupportLollipop(executor: () -> Unit) {
    doFromSdk(Build.VERSION_CODES.LOLLIPOP) {
        executor()
    }
}
