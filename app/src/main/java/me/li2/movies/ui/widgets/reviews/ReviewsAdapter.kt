/*
 * Created by Weiyi Li on 2020-04-26.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.reviews

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import me.li2.movies.data.model.MovieReviewUI

class ReviewsAdapter : ListAdapter<MovieReviewUI, ReviewViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder.create(parent)
    }

    override fun onBindViewHolder(viewHolder: ReviewViewHolder, position: Int) {
        viewHolder.bind(getItem(position), position)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieReviewUI>() {
            override fun areItemsTheSame(oldItem: MovieReviewUI, newItem: MovieReviewUI) =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieReviewUI, newItem: MovieReviewUI) =
                    oldItem == newItem
        }
    }
}
