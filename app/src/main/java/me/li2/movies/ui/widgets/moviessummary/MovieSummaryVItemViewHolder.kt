package me.li2.movies.ui.widgets.moviessummary

import android.view.ViewGroup
import android.widget.ImageView
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.subjects.PublishSubject
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.databinding.MovieSummaryVItemViewBinding

class MovieSummaryVItemViewHolder(binding: MovieSummaryVItemViewBinding,
                                  private val itemClicks: PublishSubject<Pair<ImageView, MovieItemUI>>)
    : BaseViewHolder<MovieItemUI, MovieSummaryVItemViewBinding>(binding) {

    override fun bind(item: MovieItemUI, position: Int) {
        binding.item = item
        binding.root
                .clicks()
                .throttleFirstShort()
                .map { Pair(binding.posterImageView, item) }
                .subscribe(itemClicks)
    }

    companion object {
        fun create(parent: ViewGroup,
                   itemClicks: PublishSubject<Pair<ImageView, MovieItemUI>>): MovieSummaryVItemViewHolder {
            val binding = newBindingInstance(parent, R.layout.movie_summary_v_item_view) as MovieSummaryVItemViewBinding
            return MovieSummaryVItemViewHolder(binding, itemClicks)
        }
    }
}
