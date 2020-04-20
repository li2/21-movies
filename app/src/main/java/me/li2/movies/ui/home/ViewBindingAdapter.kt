package me.li2.movies.ui.home

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import me.li2.movies.ui.home.top.TopItemUI
import me.li2.movies.ui.home.top.TopItemsAdapter

object ViewBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["app:topItems"])
    fun setTopItems(viewPager: ViewPager2, items: List<TopItemUI>?) {
        if (viewPager.adapter as? TopItemsAdapter == null) {
            viewPager.adapter = TopItemsAdapter()
        }
        val adapter = viewPager.adapter as TopItemsAdapter
        adapter.submitList(items) {
            viewPager.setCurrentItem(adapter.getCarouselInitialDisplayPosition(), false)
        }
    }
}