package me.li2.movies.ui.movies

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.li2.movies.R
import me.li2.movies.util.LinearSpacingItemDecoration

object ViewBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["app:movies"])
    fun setMovies(rv: RecyclerView, items: List<MovieItem>?) {
        if (rv.adapter as? MoviesAdapter == null) {
            rv.adapter = MoviesAdapter()
            rv.layoutManager = LinearLayoutManager(rv.context)
            rv.addItemDecoration(LinearSpacingItemDecoration(rv.context, R.dimen.movies_item_margin, RecyclerView.VERTICAL))
        }
        (rv.adapter as MoviesAdapter).submitList(items)
    }
}
