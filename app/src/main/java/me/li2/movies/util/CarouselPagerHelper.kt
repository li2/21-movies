@file:Suppress("unused")
package me.li2.movies.util

/**
 * Provide carousel page scroll ability when using ViewPager2 + ListAdapter.
 *
 * Usage: class YourListAdapter : ListAdapter<Item, ItemViewHolder>(DIFF_CALLBACK), CarouselPagerHelper {
 */
interface CarouselPagerHelper {

    /**
     * Override by ListAdapter to provide the real data set size.
     */
    val carouselDatasetSize: Int

    /**
     * Return the position in data set to allow ListAdapter.onBindViewHolder
     * to bind correct data.
     */
    fun getCarouselDataPosition(carouselPosition: Int) =
            if (carouselDatasetSize > 0) carouselPosition % carouselDatasetSize else 0

    /**
     * Override ListAdapter.getItemCount() to provide a count which can
     * simulate infinite loop effect.
     *
     * Usage: override fun getItemCount() = getCarouselPagesCount()
     */
    fun getCarouselDisplayedSize() =
            if (carouselDatasetSize > 0) Int.MAX_VALUE else 0

    /**
     * Return initial position to allow ViewPager to swipe to left.
     *
     * Usage: viewPager.setCurrentItem(adapter.getCarouselInitialPosition(), false)
     */
    fun getCarouselInitialDisplayPosition() =
            if (carouselDatasetSize > 0) 10000 / carouselDatasetSize * carouselDatasetSize else 0
}