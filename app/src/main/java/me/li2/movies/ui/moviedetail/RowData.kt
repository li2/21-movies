package me.li2.movies.ui.moviedetail

import me.li2.android.common.arch.Resource
import me.li2.movies.data.model.MovieDetailUI
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.data.model.MovieReviewUI
import me.li2.movies.ui.moviedetail.MovieDetailRowType.*

// wrapper classes to support multiple view type for movie detail screen

enum class MovieDetailRowType {
    ROW_TYPE_DETAIL,
    ROW_TYPE_REVIEWS,
    ROW_TYPE_REC_MOVIES,
}

sealed class BaseRowData(val rowType: MovieDetailRowType)

data class DetailRowData(val movieDetail: Resource<MovieDetailUI?>) : BaseRowData(ROW_TYPE_DETAIL)
data class ReviewsRowData(val reviews: List<MovieReviewUI>?) : BaseRowData(ROW_TYPE_REVIEWS)
data class RecMoviesRowData(val movies: List<MovieItemUI>?) : BaseRowData(ROW_TYPE_REC_MOVIES)
