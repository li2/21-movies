/*
 * Created by Weiyi on 2020-05-21.
 * https://github.com/li2
 */
package me.li2.movies.util

import android.os.Build
import android.transition.Transition
import android.transition.TransitionListenerAdapter
import androidx.annotation.IdRes
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform

/**
 * Call in the list fragment onViewCreated() to fix issue:
 * Navigation component: shared transitions not works when navigate from detail to list fragment.
 *
 * @see <a href="https://github.com/android/architecture-components-samples/issues/495#issuecomment-488364003">Issue 495</a>
 */
fun Fragment.fixContainerExitTransition() {
    postponeEnterTransition()
    view?.doOnPreDraw { startPostponedEnterTransition() }
}

/**
 * Call in the list fragment to hold its container to avoid it disappeared immediately.
 *
 * @param targetId id of the root view in the list fragment.
 */
fun Fragment.setUpContainerExitTransition(@IdRes targetId: Int) {
    // Use a Hold transition to keep this fragment visible beneath the MaterialContainerTransform
    // that transitions to the detail fragment. Without a Hold, this fragment would disappear
    // as soon its container is replaced with a new Fragment.
    val hold = Hold()
    // Add root view as target for the Hold so that the entire view hierarchy is held in place as
    // one instead of each child view individually. Helps keep shadows during the transition.
    hold.addTarget(targetId)
    exitTransition = hold
}

/**
 * Call in the list fragment.
 */
fun Fragment.removeContainerExitTransition() {
    exitTransition = null
}

/**
 * Call in the detail fragment.
 */
fun Fragment.setUpContainerEnterTransitions(configuration: ContainerTransformConfiguration) {
    val transform = MaterialContainerTransform(requireContext())
    if (configuration.isArcMotionEnabled) {
        transform.pathMotion = MaterialArcMotion()
    }
    sharedElementEnterTransition = transform
}

// todo weiyi why onTransitionEnd not being called?
fun Fragment.onSharedElementEnterTransitionEnd(executor: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        activity?.window?.sharedElementEnterTransition?.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition?) {
                executor.invoke()
            }
        }) ?: executor.invoke()
    } else {
        executor.invoke()
    }
}
