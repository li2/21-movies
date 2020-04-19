package me.li2.movies.util

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.rd.PageIndicatorView

fun PageIndicatorView.setViewPager2(viewPager2: ViewPager2) {
    val adapter = (viewPager2.adapter as? ListAdapter<*, *>) ?: return

    viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            setSelected(position)
        }
    })

    // not working when submitList
    adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            count = adapter.itemCount
        }
    })
}