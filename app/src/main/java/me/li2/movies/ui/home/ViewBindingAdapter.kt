package me.li2.movies.ui.home

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import me.li2.android.view.list.LinearSpacingDecoration
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.ui.home.centre.CentreItemsAdapter
import me.li2.movies.util.dpToPx

object ViewBindingAdapter {
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