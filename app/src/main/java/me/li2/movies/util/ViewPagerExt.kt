package me.li2.movies.util

import androidx.annotation.Px
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE


/**
 * For ViewPager wrapped inside a SwipeRefreshLayout, to avoid SwipeRefreshLayout being refreshed
 * when swipe ViewPager to the left/right. This mostly happens when ViewPage is the top of my fragment.
 *
 * @see <a href="https://stackoverflow.com/a/35825488/2722270">SwipeRefreshLayout intercepts with ViewPager</a>
 */
fun ViewPager2.ignorePullToRefresh(swipeRefreshLayout: SwipeRefreshLayout) {
    this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            if (!swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.isEnabled = state == SCROLL_STATE_IDLE
            }
        }
    })
}

/**
 * Show the partial preview of left and right page of ViewPager.
 *
 * @see <a href="https://proandroiddev.com/look-deep-into-viewpager2-13eb8e06e419">Look Deep Into ViewPager2</a>
 * @see <a href="https://code.luasoftware.com/tutorials/android/android-viewpager2-show-partial-preview-of-left-and-right/">Horizontal Show Partial Preview of Left and Right</a>
 */
fun ViewPager2.showPartialLeftAndRightPages(
        @Px offset: Int,
        @Px pageMargin: Int,
        transformer: ViewPager2.PageTransformer? = null,
        offScreenPageLimit: Int = 3) {
    // allow full width shown with padding
    clipToPadding = false
    // allow left/right item is not clipped
    clipChildren = false
    // make sure left/right item is rendered
    offscreenPageLimit = offScreenPageLimit

    // increase this offset to show more of left/right
    setPadding(offset, 0, offset, 0)

    setPageTransformer(CompositePageTransformer().apply {
        addTransformer(MarginPageTransformer(pageMargin))
        transformer?.let { addTransformer(transformer) }
    })
}