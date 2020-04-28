package me.li2.movies.util

import android.view.View

interface KeepRootViewAware {
    var rootView: View?
    var hasInitializedRootView: Boolean

    fun saveRootViewIfNeeded(saver: () -> View): View? {
        if (rootView == null) {
            rootView = saver()
        }
        return rootView
    }

    fun initializeRootViewIfNeeded(init: () -> Unit) {
        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            init()
        }
    }
}