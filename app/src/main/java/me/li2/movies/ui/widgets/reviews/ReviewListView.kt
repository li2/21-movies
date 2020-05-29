/*
 * Created by Weiyi Li on 2020-05-11.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.reviews

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.*
import me.li2.android.view.list.DividerItemDecoration
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.MovieReviewUI
import me.li2.movies.databinding.ReviewListViewBinding
import me.li2.movies.util.noData

class ReviewListViewHolder(binding: ReviewListViewBinding)
    : BaseViewHolder<Resource<List<MovieReviewUI>>, ReviewListViewBinding>(binding) {

    private val reviewsAdapter = ReviewsAdapter()

    init {
        binding.reviewsRecyclerView.apply {
            adapter = reviewsAdapter
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    override fun bind(item: Resource<List<MovieReviewUI>>, position: Int) {
        reviewsAdapter.submitList(item.data?.take(5))
        binding.showShimmer = item.status == LOADING && item.noData()
        binding.showComments = !item.noData()
        // update empty or error message
        binding.stateMessage = when {
            item.status == SUCCESS && item.noData() -> "oops no reviews yet"
            item.status == ERROR && item.noData() -> "\uD83D\uDE28 Wooops ${item.exception?.message}"
            else -> null
        }
    }

    companion object {
        fun create(parent: ViewGroup): ReviewListViewHolder {
            val binding = newBindingInstance(parent, R.layout.review_list_view) as ReviewListViewBinding
            return ReviewListViewHolder(binding)
        }
    }
}