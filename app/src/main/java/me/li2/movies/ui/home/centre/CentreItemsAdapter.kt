package me.li2.movies.ui.home.centre

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import me.li2.movies.ui.home.MovieItemUI

class CentreItemsAdapter : ListAdapter<MovieItemUI, CentreItemViewHolder>(DIFF_CALLBACK) {

    private val itemClicksPublish = PublishSubject.create<Pair<ImageView, MovieItemUI>>()
    internal val itemClicks = itemClicksPublish.toFlowable(BackpressureStrategy.LATEST).toObservable()!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CentreItemViewHolder {
        return CentreItemViewHolder.newInstance(parent, itemClicksPublish)
    }

    override fun onBindViewHolder(viewHolder: CentreItemViewHolder, position: Int) {
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
