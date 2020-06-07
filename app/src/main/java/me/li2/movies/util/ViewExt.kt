/*
 * Created by Weiyi Li on 2020-04-25.
 * https://github.com/li2
 */
package me.li2.movies.util

import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.rd.PageIndicatorView

fun PageIndicatorView.setViewPager2(viewPager2: ViewPager2) {
    val adapter = (viewPager2.adapter as? ListAdapter<*, *>) ?: return

    viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val realPosition = position % adapter.currentList.size
            setSelected(realPosition)
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

fun SearchView.init(menuItem: MenuItem,
                    query: String,
                    hint: String,
                    onQueryTextSubmit: (String?) -> Unit) {
    this.queryHint = hint
    this.isIconified = false
    // Expand SearchView Without Focus (Hide Keyboard)
    menuItem.expandActionView()
    this.doOnLayout { this.clearFocus() }
    // Show query (only works after expandActionView)
    this.setQuery(query, false)
    // Only query when keyboard actionSearch clicks
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            onQueryTextSubmit(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    })
}