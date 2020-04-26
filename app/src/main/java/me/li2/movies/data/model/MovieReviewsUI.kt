package me.li2.movies.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieReviewListUI(
        val reviews: List<MovieReviewUI>,
        val page: Int,
        val totalPages: Int,
        val totalResults: Int
) : Parcelable


@Parcelize
data class MovieReviewUI(
        val id: String,
        val author: String,
        val content: String
) : Parcelable