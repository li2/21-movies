package me.li2.movies.ui.widgets.moviessummary

import android.view.ViewGroup
import android.widget.ImageView
import io.reactivex.subjects.PublishSubject
import me.li2.android.common.arch.Resource
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.databinding.MovieSummaryHListViewBinding

/**
 * A RecyclerView.ViewHolder contains a horizontal list of movie summary item views.
 */
class MovieSummaryHListViewHolder(binding: MovieSummaryHListViewBinding,
                                  onMovieClicks: PublishSubject<Pair<ImageView, MovieItemUI>>,
                                  label: String)
    : BaseViewHolder<Resource<List<MovieItemUI>>?, MovieSummaryHListViewBinding>(binding) {

    init {
        binding.executePendingBindings()
        (binding.moviesRecyclerView.adapter as MovieSummaryHAdapter).itemClicks.subscribe(onMovieClicks)
        binding.label = label
    }

    override fun bind(item: Resource<List<MovieItemUI>>?, position: Int) {
        binding.movies = item?.data
        binding.isLoading = item?.status == Resource.Status.LOADING
    }

    companion object {
        fun create(parent: ViewGroup,
                   onMovieClicks: PublishSubject<Pair<ImageView, MovieItemUI>>,
                   label: String): MovieSummaryHListViewHolder {
            val binding = newBindingInstance(parent, R.layout.movie_summary_h_list_view) as MovieSummaryHListViewBinding
            return MovieSummaryHListViewHolder(binding, onMovieClicks, label)
        }
    }
}