package me.li2.movies.ui.widgets.moviessummary

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import me.li2.movies.data.model.MovieItemUI

class MovieSummaryHAdapter : ListAdapter<MovieItemUI, MovieSummaryHItemViewHolder>(DIFF_CALLBACK) {

    private val itemClicksPublish = PublishSubject.create<Pair<ImageView, MovieItemUI>>()
    internal val itemClicks = itemClicksPublish.toFlowable(BackpressureStrategy.LATEST).toObservable()!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSummaryHItemViewHolder {
        return MovieSummaryHItemViewHolder.create(parent, itemClicksPublish)
    }

    override fun onBindViewHolder(viewHolder: MovieSummaryHItemViewHolder, position: Int) {
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
