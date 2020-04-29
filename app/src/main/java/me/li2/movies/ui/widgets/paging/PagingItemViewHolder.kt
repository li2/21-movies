package me.li2.movies.ui.widgets.paging

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.subjects.PublishSubject
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.databinding.PagingItemViewBinding

class PagingItemViewHolder(binding: PagingItemViewBinding, retry: PublishSubject<Unit>)
    : BaseViewHolder<PagingState, PagingItemViewBinding>(binding) {

    init {
        binding.retryButton.clicks().throttleFirstShort().subscribe(retry)
    }

    override fun bind(item: PagingState, position: Int) {
        // loading spinner
        binding.loadingProgressBar.isVisible = item == PagingState.Loading

        // error & retry
        binding.retryButton.isVisible = item is PagingState.Error
        binding.errorTextView.isVisible = item is PagingState.Error
        if (item is PagingState.Error) {
            binding.errorTextView.text = item.error
        }

        // last page
        binding.lastPageTextView.isVisible = item is PagingState.Done
                && item.page == item.totalPages
    }

    companion object {
        fun create(parent: ViewGroup, retry: PublishSubject<Unit>): PagingItemViewHolder {
            return PagingItemViewHolder(
                    newBindingInstance(parent, R.layout.paging_item_view) as PagingItemViewBinding,
                    retry)
        }
    }
}