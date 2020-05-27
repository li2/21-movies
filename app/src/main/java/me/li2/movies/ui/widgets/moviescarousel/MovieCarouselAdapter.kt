/*
 * Created by Weiyi Li on 2020-04-20.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.moviescarousel

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.subjects.PublishSubject
import me.li2.android.view.list.CarouselPagerHelper
import me.li2.movies.data.model.MovieItemUI

class MovieCarouselAdapter : ListAdapter<MovieItemUI, MovieCarouselItemViewHolder>(DIFF_CALLBACK), CarouselPagerHelper {

    private val _onMovieClicks = PublishSubject.create<Pair<View, MovieItemUI>>()
    internal val onMovieClicks = _onMovieClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()!!

    override val carouselDatasetSize: Int
        get() = currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCarouselItemViewHolder {
        return MovieCarouselItemViewHolder.create(parent, _onMovieClicks)
    }

    override fun onBindViewHolder(viewHolder: MovieCarouselItemViewHolder, position: Int) {
        val dataPosition = getCarouselDataPosition(position)
        viewHolder.bind(getItem(dataPosition))
    }

    override fun getItemCount() = getCarouselDisplaySize()

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieItemUI>() {
            override fun areItemsTheSame(oldItem: MovieItemUI, newItem: MovieItemUI) =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieItemUI, newItem: MovieItemUI) =
                    oldItem == newItem
        }
    }
}
