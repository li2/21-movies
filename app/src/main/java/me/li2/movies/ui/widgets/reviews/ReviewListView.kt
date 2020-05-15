package me.li2.movies.ui.widgets.reviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.LOADING
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.MovieReviewUI
import me.li2.movies.databinding.ReviewListViewBinding

class ReviewListView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ReviewListViewBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.review_list_view, this, true)

    var reviews: List<MovieReviewUI>? = emptyList()
        set(value) {
            field = value
            binding.reviews = value
        }
}

class ReviewListViewHolder(binding: ReviewListViewBinding)
    : BaseViewHolder<Resource<List<MovieReviewUI>>, ReviewListViewBinding>(binding) {

    init {
        binding.executePendingBindings()
    }

    override fun bind(item: Resource<List<MovieReviewUI>>, position: Int) {
        binding.reviews = item.data?.take(3)
        binding.isLoading = item.status == LOADING
        binding.isEmpty = item.status != LOADING && item.data.isNullOrEmpty()
    }

    companion object {
        fun create(parent: ViewGroup): ReviewListViewHolder {
            val binding = newBindingInstance(parent, R.layout.review_list_view) as ReviewListViewBinding
            return ReviewListViewHolder(binding)
        }
    }
}