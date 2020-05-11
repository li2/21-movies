package me.li2.movies.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.facebook.shimmer.ShimmerFrameLayout

object ShimmerBindings {
    @JvmStatic
    @BindingAdapter("startShimmer")
    fun setStartShimmerAnimation(shimmerFrameLayout: ShimmerFrameLayout, startShimmer: Boolean) {
        if (startShimmer) {
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