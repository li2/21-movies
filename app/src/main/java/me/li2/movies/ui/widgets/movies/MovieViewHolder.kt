package me.li2.movies.ui.widgets.movies

import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.subjects.PublishSubject
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.databinding.MovieItemHorizontalViewBinding
import me.li2.movies.databinding.MovieItemVerticalViewBinding

class MovieViewHolder(binding: ViewDataBinding,
                      private val layoutType: MovieListLayoutType,
                      private val itemClicks: PublishSubject<Pair<ImageView, MovieItemUI>>)
    : BaseViewHolder<MovieItemUI, ViewDataBinding>(binding) {

    override fun bind(item: MovieItemUI, position: Int) {
        if (layoutType == MovieListLayoutType.LINEAR_LAYOUT_VERTICAL) {
            (binding as MovieItemVerticalViewBinding).let {
                binding.item = item
                binding.root
                        .clicks()
                        .throttleFirstShort()
                        .map { Pair(binding.posterImageView, item) }
                        .subscribe(itemClicks)
            }
        } else {
            (binding as MovieItemHorizontalViewBinding).let {
                binding.item = item
                binding.root
                        .clicks()
                        .throttleFirstShort()
                        .map { Pair(binding.posterImageView, item) }
                        .subscribe(itemClicks)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup,
                   layoutType: MovieListLayoutType,
                   itemClicks: PublishSubject<Pair<ImageView, MovieItemUI>>): MovieViewHolder {
            val binding = newBindingInstance(parent, layoutType.getLayoutResId())
            return MovieViewHolder(binding, layoutType, itemClicks)
        }
    }
}