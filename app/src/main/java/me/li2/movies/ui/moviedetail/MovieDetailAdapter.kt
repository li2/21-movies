/*
 * Created by Weiyi Li on 2020-05-11.
 * https://github.com/li2
 */
package me.li2.movies.ui.moviedetail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import me.li2.movies.data.model.CreditUI
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.data.model.Trailer
import me.li2.movies.ui.moviedetail.MovieDetailRowType.*
import me.li2.movies.ui.movies.MoviesCategory
import me.li2.movies.ui.widgets.credits.CreditListViewHolder
import me.li2.movies.ui.widgets.movies.MovieListViewHolder
import me.li2.movies.ui.widgets.reviews.ReviewListViewHolder
import me.li2.movies.ui.widgets.trailers.TrailerListViewHolder

class MovieDetailAdapter : ListAdapter<BaseRowData, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private val _onSaveClicks = PublishSubject.create<Int>()
    val onSaveClicks: Observable<Int> = _onSaveClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()

    private val _onPosterClicks = PublishSubject.create<Pair<View, String>>()
    val onPosterClicks: Observable<Pair<View, String>> = _onPosterClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()

    private val _onTrailerClicks = PublishSubject.create<Trailer>()
    val onTrailerClicks: Observable<Trailer> = _onTrailerClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()

    private val _onCategoryClicks = PublishSubject.create<MoviesCategory>()
    val onCategoryClicks: Observable<MoviesCategory> = _onCategoryClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()

    private val _onCreditClicks = PublishSubject.create<Pair<View, CreditUI>>()
    val onCreditClicks: Observable<Pair<View, CreditUI>> = _onCreditClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()

    private val _onRecMovieClicks = PublishSubject.create<Pair<View, MovieItemUI>>()
    val onRecMovieClicks: Observable<Pair<View, MovieItemUI>> = _onRecMovieClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()

    private val _onMoreRecClicks = PublishSubject.create<Unit>()
    val onMoreRecClicks = _onMoreRecClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()!!

    override fun getItemViewType(position: Int): Int {
        return getItem(position).rowType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ROW_TYPE_DETAIL.ordinal -> MovieDetailViewHolder.create(parent, _onSaveClicks, _onCategoryClicks, _onPosterClicks)
            ROW_TYPE_TRAILERS.ordinal -> TrailerListViewHolder.create(parent, _onTrailerClicks)
            ROW_TYPE_CREDITS.ordinal -> CreditListViewHolder.create(parent, _onCreditClicks)
            ROW_TYPE_REVIEWS.ordinal -> ReviewListViewHolder.create(parent)
            ROW_TYPE_REC_MOVIES.ordinal -> MovieListViewHolder.create(parent, _onRecMovieClicks, _onMoreRecClicks, "Recommendations")
            else -> throw RuntimeException("Unexpected view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        when (holder) {
            is MovieDetailViewHolder -> holder.bind((data as DetailRowData).movieDetail, position)
            is TrailerListViewHolder -> holder.bind((data as TrailersRowData).trailers, position)
            is CreditListViewHolder -> holder.bind((data as CreditsRowData).credits, position)
            is ReviewListViewHolder -> holder.bind((data as ReviewsRowData).reviews, position)
            is MovieListViewHolder -> holder.bind((data as RecMoviesRowData).movies, position)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BaseRowData>() {
            override fun areItemsTheSame(oldItem: BaseRowData, newItem: BaseRowData): Boolean {
                // for RecyclerView contains multiple view type, return true if they are the same data type
                if (oldItem is DetailRowData && newItem is DetailRowData) {
                    return true
                }
                if (oldItem is TrailersRowData && newItem is TrailersRowData) {
                    return true
                }
                if (oldItem is CreditsRowData && newItem is CreditsRowData) {
                    return true
                }
                if (oldItem is ReviewsRowData && newItem is ReviewsRowData) {
                    return true
                }
                if (oldItem is RecMoviesRowData && newItem is RecMoviesRowData) {
                    return true
                }
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: BaseRowData, newItem: BaseRowData): Boolean {
                return oldItem == newItem
            }
        }
    }
}