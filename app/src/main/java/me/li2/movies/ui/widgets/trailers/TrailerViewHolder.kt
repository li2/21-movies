/*
 * Created by Weiyi Li on 2020-05-25.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.trailers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.subjects.PublishSubject
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.Trailer
import me.li2.movies.databinding.TrailerItemViewBinding

class TrailerViewHolder(binding: TrailerItemViewBinding,
                        private val onTrailerClicks: PublishSubject<Trailer>)
    : BaseViewHolder<Trailer, TrailerItemViewBinding>(binding) {

    override fun bind(item: Trailer, position: Int) {
        binding.item = item
        binding.youtubeButton.clicks().throttleFirstShort()
                .map { item }
                .subscribe(onTrailerClicks)
    }

    companion object {
        fun create(parent: ViewGroup,
                   onTrailerClicks: PublishSubject<Trailer>): TrailerViewHolder {
            val binding = DataBindingUtil.inflate<TrailerItemViewBinding>(LayoutInflater.from(parent.context), R.layout.trailer_item_view, parent, false)
            return TrailerViewHolder(binding, onTrailerClicks)
        }
    }
}