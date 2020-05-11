package me.li2.movies.ui.moviedetail

import android.annotation.SuppressLint
import android.view.ViewGroup
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.li2.android.common.logic.orFalse
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.GenreUI
import me.li2.movies.data.model.MovieDetailUI
import me.li2.movies.databinding.MovieDetailViewBinding
import me.li2.movies.util.watchYoutubeVideo

class MovieDetailViewHolder(binding: MovieDetailViewBinding,
                            private val onRateClicks: PublishSubject<Unit>,
                            private val onGenreClicks: PublishSubject<GenreUI>)
    : BaseViewHolder<MovieDetailUI?, MovieDetailViewBinding>(binding) {

    init {
        initView()
    }

    override fun bind(item: MovieDetailUI?, position: Int) {
        binding.movieDetail = item
    }

    @SuppressLint("CheckResult")
    private fun initView() {
        binding.youtubeButton.clicks().throttleFirstShort().subscribe {
            binding.movieDetail?.youtubeTrailerUrl?.let {
                binding.youtubeButton.context.watchYoutubeVideo(it)
            }
        }

        Observable.merge(
                binding.movieOverviewTextView.clicks().throttleFirstShort(),
                binding.movieOverviewExpandTextView.clicks().throttleFirstShort()
        ).subscribe {
            binding.isOverviewExpanded = !binding.isOverviewExpanded.orFalse()
        }

        binding.rateMovieButton.clicks().throttleFirstShort().subscribe(onRateClicks)

        binding.genresGroupView.genreClicks().throttleFirstShort().subscribe(onGenreClicks)
    }

    companion object {
        fun create(parent: ViewGroup,
                   onRateClicks: PublishSubject<Unit>,
                   onGenreClicks: PublishSubject<GenreUI>): MovieDetailViewHolder {
            val binding = newBindingInstance(parent, R.layout.movie_detail_view) as MovieDetailViewBinding
            return MovieDetailViewHolder(binding, onRateClicks, onGenreClicks)
        }
    }
}