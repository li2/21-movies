/*
 * Created by Weiyi Li on 2020-05-11.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.reviews

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.LOADING
import me.li2.android.view.list.DividerItemDecoration
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.MovieReviewUI
import me.li2.movies.databinding.ReviewListViewBinding

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
        binding.isLoading = item.status == LOADING && item.data.isNullOrEmpty()
        binding.isEmpty = item.status != LOADING && item.data.isNullOrEmpty()
    }

    companion object {
        fun create(parent: ViewGroup): ReviewListViewHolder {
            val binding = newBindingInstance(parent, R.layout.review_list_view) as ReviewListViewBinding
            return ReviewListViewHolder(binding)
        }
    }
}