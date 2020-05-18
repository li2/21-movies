/*
 * Created by Weiyi Li on 2020-04-26.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.reviews

import android.view.ViewGroup
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.MovieReviewUI
import me.li2.movies.databinding.ReviewItemViewBinding

class ReviewViewHolder(binding: ReviewItemViewBinding)
    : BaseViewHolder<MovieReviewUI, ReviewItemViewBinding>(binding) {

    override fun bind(item: MovieReviewUI, position: Int) {
        binding.item = item
    }

    companion object {
        fun create(parent: ViewGroup): ReviewViewHolder {
            return ReviewViewHolder(newBindingInstance(parent, R.layout.review_item_view) as ReviewItemViewBinding)
        }
    }
}
