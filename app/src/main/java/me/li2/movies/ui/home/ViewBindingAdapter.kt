package me.li2.movies.ui.home

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import me.li2.movies.ui.home.top.TopItemUI
import me.li2.movies.ui.home.top.TopItemsAdapter

object ViewBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["app:topItems"])
    fun setTopItems(rv: ViewPager2, items: List<TopItemUI>?) {
        if (rv.adapter as? TopItemsAdapter == null) {
            rv.adapter = TopItemsAdapter()
        }
        (rv.adapter as TopItemsAdapter).submitList(items)
    }
}