package me.li2.movies.ui.movies

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.databinding.MovieItemViewBinding

class MoviesAdapter : ListAdapter<MovieItem, MovieItemViewHolder>(DIFF_CALLBACK) {
    private val movieClicksPublish = PublishSubject.create<Pair<ImageView, MovieItem>>()
    internal val movieClicks = movieClicksPublish.toFlowable(BackpressureStrategy.LATEST).toObservable()!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        return MovieItemViewHolder.newInstance(parent, movieClicksPublish)
    }

    override fun onBindViewHolder(viewHolder: MovieItemViewHolder, position: Int) {
        viewHolder.bind(getItem(position), position)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem) =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem) =
                    oldItem == newItem
        }
    }
}

class MovieItemViewHolder(binding: MovieItemViewBinding,
                          private val movieClicks: PublishSubject<Pair<ImageView, MovieItem>>)
    : BaseViewHolder<MovieItem, MovieItemViewBinding>(binding) {

    override fun bind(item: MovieItem, position: Int) {
        binding.movieItem = item
        binding.root
                .clicks()
                .throttleFirstShort()
                .map { Pair(binding.ivPoster, item) }
                .subscribe(movieClicks)
    }

    companion object {
        fun newInstance(parent: ViewGroup,
                        movieClicks: PublishSubject<Pair<ImageView, MovieItem>>): MovieItemViewHolder {
            return MovieItemViewHolder(newBindingInstance(parent, R.layout.movie_item_view) as MovieItemViewBinding, movieClicks)
        }
    }
}
