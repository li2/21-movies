package me.li2.movies.ui.home.centre

import android.view.ViewGroup
import android.widget.ImageView
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.subjects.PublishSubject
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.databinding.HomeCentreItemViewBinding
import me.li2.movies.ui.home.MovieItemUI

class CentreItemViewHolder(binding: HomeCentreItemViewBinding,
                           private val itemClicks: PublishSubject<Pair<ImageView, MovieItemUI>>)
    : BaseViewHolder<MovieItemUI, HomeCentreItemViewBinding>(binding) {

    override fun bind(item: MovieItemUI, position: Int) {
        binding.item = item
        binding.root
                .clicks()
                .throttleFirstShort()
                .map { Pair(binding.ivPoster, item) }
                .subscribe(itemClicks)
    }

    companion object {
        fun newInstance(parent: ViewGroup,
                        itemClicks: PublishSubject<Pair<ImageView, MovieItemUI>>): CentreItemViewHolder {
            return CentreItemViewHolder(newBindingInstance(parent, R.layout.home_centre_item_view) as HomeCentreItemViewBinding, itemClicks)
        }
    }
}
