package me.li2.movies.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

/**
 * The scrollable view needs to wrapped in a CoordinatorLayout, which allows you to assign a
 * Behavior to its direct child views. The Behavior intercepts all scroll events that happen
 * in the direct children of the CoordinatorLayout.
 *
 * To assign the Behavior to the NestedScrollView we need to use the layout_behavior xml attribute
 * and pass in the full class name of the Behavior we want to use.
 *
 * https://stackoverflow.com/a/40775953/2722270
 */
class OverScrollBounceBehavior(context: Context, attributeSet: AttributeSet)
    : CoordinatorLayout.Behavior<View>() {

    private var overScrollY = 0

    override fun onStartNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: View,
            directTargetChild: View,
            target: View,
            axes: Int,
            type: Int): Boolean {
        overScrollY = 0
        return true
    }

    override fun onNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: View,
            target: View,
            dxConsumed: Int,
            dyConsumed: Int,
            dxUnconsumed: Int,
            dyUnconsumed: Int,
            type: Int,
            consumed: IntArray) {
        if (dyUnconsumed == 0) {
            return
        }

        overScrollY -= (dyUnconsumed / OVER_SCROLL_AREA)
        val group = target as ViewGroup
        val count = group.childCount
        for (i in 0 until count) {
            val view = group.getChildAt(i)
            view.translationY = overScrollY.toFloat()
        }
    }

    override fun onStopNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: View,
            target: View,
            type: Int) {
        // Smooth animate to 0 when the user stops scrolling
        moveToDefPosition(target)
    }

    override fun onNestedPreFling(
            coordinatorLayout: CoordinatorLayout,
            child: View,
            target: View,
            velocityX: Float,
            velocityY: Float): Boolean {
        // Scroll view by inertia when current position equals to 0
        if (overScrollY == 0) {
            return false
        }
        // Smooth animate to 0 when user fling view
        moveToDefPosition(target)
        return true
    }

    private fun moveToDefPosition(target: View) {
        val group = target as ViewGroup
        val count = group.childCount
        for (i in 0 until count) {
            val view = group.getChildAt(i)
            ViewCompat.animate(view)
                    .translationY(0f)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()
        }
    }

    companion object {
        private const val OVER_SCROLL_AREA = 4
    }
}