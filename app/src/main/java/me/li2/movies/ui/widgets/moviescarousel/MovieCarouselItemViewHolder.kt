/*
 * Created by Weiyi Li on 2020-04-20.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.moviescarousel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.subjects.PublishSubject
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.databinding.MovieCarouselItemViewBinding

class MovieCarouselItemViewHolder(root: View,
                                  private val binding: MovieCarouselItemViewBinding?,
                                  private val itemClicks: PublishSubject<Pair<View, MovieItemUI>>)
    : RecyclerView.ViewHolder(root) {

    fun bind(item: MovieItemUI) {
        binding?.let {
            binding.item = item
            binding.root
                    .clicks()
                    .throttleFirstShort()
                    .map { Pair(binding.movieItemContainer, item) }
                    .subscribe(itemClicks)
        }
    }

    companion object {
        fun create(parent: ViewGroup,
                   itemClicks: PublishSubject<Pair<View, MovieItemUI>>): MovieCarouselItemViewHolder {
            val root = LayoutInflater.from(parent.context).inflate(R.layout.movie_carousel_item_view, parent, false)
            // render problem: Pages must fill the whole ViewPager2 (use match_parent)
            // Android Preview is not taking the match_parent assigned in xml to attach views.
            // To fix it, set layout params as MATCH_PARENT explicitly. noteweiyi
            // https://stackoverflow.com/a/58404428/2722270
            root.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

            val binding = if (!parent.isInEditMode) {
                DataBindingUtil.bind<MovieCarouselItemViewBinding>(root)
            } else {
                null
            }
            return MovieCarouselItemViewHolder(root, binding, itemClicks)
        }
    }
}
