package me.li2.movies.ui.widgets.movies

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.ui.widgets.movies.MovieListLayoutType.LINEAR_LAYOUT_HORIZONTAL

class MovieListAdapter(layoutType: MovieListLayoutType) : ListAdapter<MovieItemUI, MovieViewHolder>(DIFF_CALLBACK) {

    private val _onMovieClicks = PublishSubject.create<Pair<ImageView, MovieItemUI>>()
    val onMovieClicks = _onMovieClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()!!

    var layoutType: MovieListLayoutType = LINEAR_LAYOUT_HORIZONTAL
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        this.layoutType = layoutType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.create(parent, layoutType, _onMovieClicks)
    }

    override fun onBindViewHolder(viewHolder: MovieViewHolder, position: Int) {
        viewHolder.bind(getItem(position), position)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieItemUI>() {
            override fun areItemsTheSame(oldItem: MovieItemUI, newItem: MovieItemUI) =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieItemUI, newItem: MovieItemUI) =
                    oldItem == newItem
        }
    }
}