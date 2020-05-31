/*
 * Created by Weiyi Li on 2020-04-27.
 * https://github.com/li2
 */
package me.li2.movies.util

import android.view.View

interface RootViewStore {
    var rootView: View?
    var hasInitializedRootView: Boolean
    var hasInitializedOptionsMenu: Boolean

    fun createRootViewIfNeeded(saver: () -> View): View? {
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

    fun initializeOptionsMenuIfNeeded(init: () -> Unit) {
        if (!hasInitializedOptionsMenu) {
            hasInitializedOptionsMenu = true
            init()
        }
    }
}