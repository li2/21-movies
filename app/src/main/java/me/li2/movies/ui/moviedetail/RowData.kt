/*
 * Created by Weiyi Li on 2020-05-11.
 * https://github.com/li2
 */
package me.li2.movies.ui.moviedetail

import me.li2.android.common.arch.Resource
import me.li2.movies.data.model.*
import me.li2.movies.ui.moviedetail.MovieDetailRowType.*

// wrapper classes to support multiple view type for movie detail screen

enum class MovieDetailRowType {
    ROW_TYPE_DETAIL,
    ROW_TYPE_TRAILERS,
    ROW_TYPE_CREDITS,
    ROW_TYPE_REVIEWS,
    ROW_TYPE_REC_MOVIES,
}

sealed class BaseRowData(val rowType: MovieDetailRowType)

data class DetailRowData(val movieDetail: Resource<MovieDetailUI>) : BaseRowData(ROW_TYPE_DETAIL)
data class TrailersRowData(val trailers: Resource<List<Trailer>>) : BaseRowData(ROW_TYPE_TRAILERS)
data class CreditsRowData(val credits: Resource<CreditListUI>) : BaseRowData(ROW_TYPE_CREDITS)
data class ReviewsRowData(val reviews: Resource<List<MovieReviewUI>>) : BaseRowData(ROW_TYPE_REVIEWS)
data class RecMoviesRowData(val movies: Resource<List<MovieItemUI>>) : BaseRowData(ROW_TYPE_REC_MOVIES)
