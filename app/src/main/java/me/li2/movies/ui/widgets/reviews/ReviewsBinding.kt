/*
 * Created by Weiyi Li on 2020-04-26.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.reviews

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import me.li2.android.view.list.DividerItemDecoration
import me.li2.movies.data.model.MovieReviewUI

object ReviewsBinding {
    @JvmStatic
    @BindingAdapter(value = ["reviews"])
    fun setReviews(rv: RecyclerView, items: List<MovieReviewUI>?) {
        if (rv.adapter as? ReviewsAdapter == null) {
            rv.adapter = ReviewsAdapter()
            rv.addItemDecoration(DividerItemDecoration(rv.context, VERTICAL))
            rv.layoutManager = LinearLayoutManager(rv.context, VERTICAL, false)
        }
        (rv.adapter as ReviewsAdapter).submitList(items)
    }
}