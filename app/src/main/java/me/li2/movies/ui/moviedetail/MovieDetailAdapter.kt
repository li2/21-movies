/*
 * Created by Weiyi Li on 2020-05-11.
 * https://github.com/li2
 */
package me.li2.movies.ui.moviedetail

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.li2.movies.data.model.GenreUI
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.ui.moviedetail.MovieDetailRowType.*
import me.li2.movies.ui.widgets.credits.CreditListViewHolder
import me.li2.movies.ui.widgets.movies.MovieListViewHolder
import me.li2.movies.ui.widgets.reviews.ReviewListViewHolder

class MovieDetailAdapter : ListAdapter<BaseRowData, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private val _onRateClicks = PublishSubject.create<Unit>()
    val onRateClicks: Observable<Unit> = _onRateClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()

    private val _onGenreClicks = PublishSubject.create<GenreUI>()
    val onGenreClicks: Observable<GenreUI> = _onGenreClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()

    private val _onRecMovieClicks = PublishSubject.create<Pair<ImageView, MovieItemUI>>()
    val onRecMovieClicks: Observable<Pair<ImageView, MovieItemUI>> = _onRecMovieClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()

    override fun getItemViewType(position: Int): Int {
        return getItem(position).rowType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ROW_TYPE_DETAIL.ordinal -> MovieDetailViewHolder.create(parent, _onRateClicks, _onGenreClicks)
            ROW_TYPE_CREDITS.ordinal -> CreditListViewHolder.create(parent)
            ROW_TYPE_REVIEWS.ordinal -> ReviewListViewHolder.create(parent)
            ROW_TYPE_REC_MOVIES.ordinal -> MovieListViewHolder.create(parent, _onRecMovieClicks, "Recommendations")
            else -> throw RuntimeException("Unexpected view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        when (holder) {
            is MovieDetailViewHolder -> holder.bind((data as DetailRowData).movieDetail, position)
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