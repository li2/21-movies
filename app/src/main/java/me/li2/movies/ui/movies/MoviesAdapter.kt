package me.li2.movies.ui.movies

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import me.li2.movies.R
import me.li2.movies.databinding.VhMovieBinding
import me.li2.movies.util.throttleFirstShort

class MoviesAdapter : ListAdapter<MovieItem, MovieVH>(DIFF_CALLBACK) {
    private val itemClicksPublish = PublishSubject.create<MovieItem>()
    internal val itemClicks = itemClicksPublish.toFlowable(BackpressureStrategy.LATEST).toObservable()!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MovieVH(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.vh_movie, parent, false))

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(vh: MovieVH, position: Int) {
        vh.bindData(getItem(position), itemClicksPublish)
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

class MovieVH(private val binding: VhMovieBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("CheckResult")
    fun bindData(data: MovieItem, itemClicks: PublishSubject<MovieItem>) {
        binding.movieItem = data
        binding.root.clicks().throttleFirstShort().map { data }.subscribe(itemClicks)
    }
}
