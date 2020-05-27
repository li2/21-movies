/*
 * Created by Weiyi Li on 2020-05-18.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.trailers

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.reactivex.rxjava3.subjects.PublishSubject
import me.li2.movies.data.model.Trailer

class TrailerListAdapter(private val onTrailerClicks: PublishSubject<Trailer>)
    : ListAdapter<Trailer, TrailerViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        return TrailerViewHolder.create(parent, onTrailerClicks)
    }

    override fun onBindViewHolder(viewHolder: TrailerViewHolder, position: Int) {
        viewHolder.bind(getItem(position), position)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Trailer>() {
            override fun areItemsTheSame(oldItem: Trailer, newItem: Trailer) =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Trailer, newItem: Trailer) =
                    oldItem == newItem
        }
    }
}