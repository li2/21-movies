package me.li2.movies.ui.home.top

import android.view.ViewGroup
import android.widget.ImageView
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.subjects.PublishSubject
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.databinding.HomeTopItemViewBinding

class TopItemViewHolder(binding: HomeTopItemViewBinding,
                        private val itemClicks: PublishSubject<Pair<ImageView, TopItemUI>>)
    : BaseViewHolder<TopItemUI, HomeTopItemViewBinding>(binding) {

    override fun bind(item: TopItemUI, position: Int) {
        binding.item = item
        binding.root
                .clicks()
                .throttleFirstShort()
                .map { Pair(binding.ivPoster, item) }
                .subscribe(itemClicks)
    }

    companion object {
        fun newInstance(parent: ViewGroup,
                        itemClicks: PublishSubject<Pair<ImageView, TopItemUI>>): TopItemViewHolder {
            return TopItemViewHolder(newBindingInstance(parent, R.layout.home_top_item_view) as HomeTopItemViewBinding, itemClicks)
        }
    }
}
