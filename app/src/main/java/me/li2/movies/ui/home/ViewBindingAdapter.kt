package me.li2.movies.ui.home

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.viewpager2.widget.ViewPager2
import me.li2.android.view.list.LinearSpacingDecoration
import me.li2.movies.ui.home.centre.CentreItemsAdapter
import me.li2.movies.ui.home.top.TopItemsAdapter
import me.li2.movies.util.dpToPx

object ViewBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["app:topItems"])
    fun setTopItems(viewPager: ViewPager2, items: List<MovieItemUI>?) {
        if (viewPager.adapter as? TopItemsAdapter == null) {
            viewPager.adapter = TopItemsAdapter()
        }
        val adapter = viewPager.adapter as TopItemsAdapter
        adapter.submitList(items) {
            viewPager.setCurrentItem(adapter.getCarouselInitialDisplayPosition(), false)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["app:centreItems"])
    fun setCentreItems(rv: RecyclerView, items: List<MovieItemUI>?) {
        if (rv.adapter as? CentreItemsAdapter == null) {
            rv.adapter = CentreItemsAdapter()
            rv.layoutManager = LinearLayoutManager(rv.context, HORIZONTAL, false)
            rv.addItemDecoration(LinearSpacingDecoration(HORIZONTAL, 16.dpToPx(rv.context)))
        }
        (rv.adapter as CentreItemsAdapter).submitList(items)
    }
}