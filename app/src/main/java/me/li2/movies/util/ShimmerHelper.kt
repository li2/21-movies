package me.li2.movies.util

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout

interface ShimmerHelper {

    val shimmerLayouts: List<ShimmerFrameLayout>

    fun startShimmer() {
        shimmerLayouts.forEach {
            it.startShimmerAnimation()
            it.visibility = View.VISIBLE
        }
    }

    fun stopShimmer() {
        shimmerLayouts.forEach {
            it.stopShimmerAnimation()
            it.visibility = View.GONE
        }
    }
}