package me.li2.movies.util

import android.Manifest
import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayout
import com.tbruyelle.rxpermissions2.RxPermissions
import org.jetbrains.anko.toast

fun Activity.setToolbar(toolbar: Toolbar, title: String = "", @DrawableRes iconId: Int? = null) {
    (this as? AppCompatActivity)?.apply {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setTitle(title)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)
        }
        iconId?.run { toolbar.setNavigationIcon(this) }
    }
}

fun TabLayout.forEachIndexed(action: (index: Int, tab: TabLayout.Tab) -> Unit) {
    for (index in 0 until tabCount) {
        getTabAt(index)?.let { tab ->
            action(index, tab)
        }
    }
}

fun Fragment.toast(message: String) = context?.toast(message)

fun Fragment.navController() = NavHostFragment.findNavController(this)

fun Fragment.ifCameraPermissionGranted(onGranted: () -> Unit) {
    activity?.run {
        RxPermissions(this).request(Manifest.permission.CAMERA).subscribe { granted ->
            if (granted) {
                onGranted()
            } else {
                toast("Camera permission is denied")
            }
        }
    }
}
