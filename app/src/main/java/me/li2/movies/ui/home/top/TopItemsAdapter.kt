package me.li2.movies.ui.home.top

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import me.li2.movies.util.CarouselPagerHelper

class TopItemsAdapter : ListAdapter<TopItemUI, TopItemViewHolder>(DIFF_CALLBACK), CarouselPagerHelper {

    private val itemClicksPublish = PublishSubject.create<Pair<ImageView, TopItemUI>>()
    internal val itemClicks = itemClicksPublish.toFlowable(BackpressureStrategy.LATEST).toObservable()!!

    override val carouselDatasetSize: Int
        get() = currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopItemViewHolder {
        return TopItemViewHolder.newInstance(parent, itemClicksPublish)
    }

    override fun onBindViewHolder(viewHolder: TopItemViewHolder, position: Int) {
        val dataPosition = getCarouselDataPosition(position)
        viewHolder.bind(getItem(dataPosition), dataPosition)
    }

    override fun getItemCount() = getCarouselDisplayedSize()

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TopItemUI>() {
            override fun areItemsTheSame(oldItem: TopItemUI, newItem: TopItemUI) =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: TopItemUI, newItem: TopItemUI) =
                    oldItem == newItem
        }
    }
}
