package me.li2.movies.ui.widgets.moviescarousel

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import me.li2.android.view.list.CarouselPagerHelper
import me.li2.movies.data.model.MovieItemUI

class MovieCarouselAdapter : ListAdapter<MovieItemUI, MovieCarouselItemViewHolder>(DIFF_CALLBACK), CarouselPagerHelper {

    private val itemClicksPublish = PublishSubject.create<Pair<ImageView, MovieItemUI>>()
    internal val itemClicks = itemClicksPublish.toFlowable(BackpressureStrategy.LATEST).toObservable()!!

    override val carouselDatasetSize: Int
        get() = currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCarouselItemViewHolder {
        return MovieCarouselItemViewHolder.newInstance(parent, itemClicksPublish)
    }

    override fun onBindViewHolder(viewHolder: MovieCarouselItemViewHolder, position: Int) {
        val dataPosition = getCarouselDataPosition(position)
        viewHolder.bind(getItem(dataPosition), dataPosition)
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
