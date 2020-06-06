/*
 * Created by Weiyi Li on 2020-05-11.
 * https://github.com/li2
 */
package me.li2.movies.ui.moviedetail

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.LOADING
import me.li2.android.common.logic.orFalse
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.MovieDetailUI
import me.li2.movies.databinding.MovieDetailViewBinding
import me.li2.movies.ui.movies.MoviesCategory

class MovieDetailViewHolder(binding: MovieDetailViewBinding,
                            private val onSaveClicks: PublishSubject<Int>,
                            private val onCategoryClicks: PublishSubject<MoviesCategory>,
                            private val onPosterClicks: PublishSubject<Pair<View, String>>)
    : BaseViewHolder<Resource<MovieDetailUI>, MovieDetailViewBinding>(binding) {

    init {
        initView()
    }

    override fun bind(item: Resource<MovieDetailUI>, position: Int) {
        binding.movieDetail = item.data
        binding.categories = item.data?.genresToCategories()
        binding.isLoadingMovieDetail = item.status == LOADING

        item.data?.posterOriginalUrl?.let { url ->
            ViewCompat.setTransitionName(binding.posterImageView, url)
            binding.posterImageView.clicks().throttleFirstShort()
                    .map { Pair(binding.posterImageView, url) }
                    .subscribe(onPosterClicks)
        }

        item.data?.id?.let { movieId ->
            binding.saveButton.clicks().throttleFirstShort().map { movieId }.subscribe(onSaveClicks)
        }
    }

    @SuppressLint("CheckResult")
    private fun initView() {
        Observable.merge(
                binding.movieOverviewTextView.clicks().throttleFirstShort(),
                binding.movieOverviewExpandTextView.clicks().throttleFirstShort()
        ).subscribe {
            binding.isOverviewExpanded = !binding.isOverviewExpanded.orFalse()
        }

        binding.categoriesGroupView.onCategoryClicks.throttleFirstShort().subscribe(onCategoryClicks)
    }

    companion object {
        fun create(parent: ViewGroup,
                   onSaveClicks: PublishSubject<Int>,
                   onCategoryClicks: PublishSubject<MoviesCategory>,
                   onPosterClicks: PublishSubject<Pair<View, String>>): MovieDetailViewHolder {
            val binding = newBindingInstance(parent, R.layout.movie_detail_view) as MovieDetailViewBinding
            return MovieDetailViewHolder(binding, onSaveClicks, onCategoryClicks, onPosterClicks)
        }
    }
}