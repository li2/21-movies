/*
 * Created by Weiyi Li on 2020-04-27.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.moviescarousel

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import me.li2.movies.data.model.MovieItemUI

object MovieCarouselBinding {
    @JvmStatic
    @BindingAdapter(value = ["movieCarouselItems"])
    fun setMovieCarouselItems(viewPager: ViewPager2, items: List<MovieItemUI>?) {
        if (viewPager.adapter as? MovieCarouselAdapter == null) {
            viewPager.adapter = MovieCarouselAdapter()
        }
        val adapter = viewPager.adapter as MovieCarouselAdapter
        adapter.submitList(items) {
            if (viewPager.currentItem == 0) {
                viewPager.setCurrentItem(adapter.getCarouselInitialDisplayPosition(), false)
            }
        }
    }
}