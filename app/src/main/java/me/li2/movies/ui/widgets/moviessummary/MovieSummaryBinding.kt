package me.li2.movies.ui.widgets.moviessummary

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import me.li2.android.view.list.LinearSpacingDecoration
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.util.dpToPx

object MovieSummaryBinding {
    @JvmStatic
    @BindingAdapter(value = ["app:movieSummaryHItems"])
    fun setMovieSummaryHorizontalItems(rv: RecyclerView, items: List<MovieItemUI>?) {
        if (rv.adapter as? MovieSummaryHAdapter == null) {
            rv.adapter = MovieSummaryHAdapter()
            rv.layoutManager = LinearLayoutManager(rv.context, HORIZONTAL, false)
            rv.addItemDecoration(LinearSpacingDecoration(HORIZONTAL, 16.dpToPx(rv.context)))
        }
        (rv.adapter as MovieSummaryHAdapter).submitList(items)
    }

    @JvmStatic
    @BindingAdapter(value = ["app:movieSummaryVItems"])
    fun setMovieSummaryVerticalItems(rv: RecyclerView, items: List<MovieItemUI>?) {
        if (rv.adapter as? MovieSummaryVAdapter == null) {
            rv.adapter = MovieSummaryVAdapter()
            rv.layoutManager = LinearLayoutManager(rv.context)
            rv.addItemDecoration(DividerItemDecoration(rv.context, RecyclerView.VERTICAL))
        }
        (rv.adapter as MovieSummaryVAdapter).submitList(items)
    }
}