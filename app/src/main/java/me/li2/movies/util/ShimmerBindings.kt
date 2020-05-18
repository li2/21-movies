/*
 * Created by Weiyi Li on 2020-05-11.
 * https://github.com/li2
 */
package me.li2.movies.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import me.li2.android.common.logic.orFalse

object ShimmerBindings {
    @JvmStatic
    @BindingAdapter("startShimmer")
    fun setStartShimmerAnimation(shimmerFrameLayout: ShimmerFrameLayout, startShimmer: Boolean?) {
        if (startShimmer.orFalse()) {
            shimmerFrameLayout.startShimmerAnimation()
            shimmerFrameLayout.visibility = View.VISIBLE
        } else {
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.visibility = View.GONE
        }
    }
}

fun ShimmerFrameLayout.showAnimation(visible: Boolean) {
    if (visible) {
        this.startShimmerAnimation()
        this.visibility = View.VISIBLE
    } else {
        this.stopShimmerAnimation()
        this.visibility = View.GONE
    }
}