/*
 * Created by Weiyi Li on 2020-05-18.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.movies

import android.view.ViewGroup
import android.widget.ImageView
import io.reactivex.subjects.PublishSubject
import me.li2.android.common.arch.Resource
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.databinding.MovieListViewholderBinding

class MovieListViewHolder(binding: MovieListViewholderBinding,
                          onMovieClicks: PublishSubject<Pair<ImageView, MovieItemUI>>,
                          label: String)
    : BaseViewHolder<Resource<List<MovieItemUI>>?, MovieListViewholderBinding>(binding) {

    init {
        binding.label = label
        binding.layoutType = MovieListLayoutType.LINEAR_LAYOUT_HORIZONTAL
        binding.movieListView.onMovieClicks.subscribe(onMovieClicks)
    }

    override fun bind(item: Resource<List<MovieItemUI>>?, position: Int) {
        binding.movies = item
    }

    companion object {
        // create ViewHolder layout as a wrapper of MovieItemListView,
        // because view of ViewHolder needs to be added to parent later, NOT RIGHT NOW.
        // that's why MovieItemListViewHolder(MovieListView(parent.context)) not works, because it inflates its layout and add to parent(this) right now.
        // https://stackoverflow.com/a/45809756/2722270 21noteWy
        fun create(parent: ViewGroup,
                   onMovieClicks: PublishSubject<Pair<ImageView, MovieItemUI>>,
                   label: String): MovieListViewHolder {
            val binding = newBindingInstance(parent, R.layout.movie_list_viewholder) as MovieListViewholderBinding
            return MovieListViewHolder(binding, onMovieClicks, label)
        }
    }
}