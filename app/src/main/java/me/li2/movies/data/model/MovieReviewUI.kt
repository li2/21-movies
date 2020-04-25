package me.li2.movies.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieReviewUI(
        val author: String,
        val content: String
) : Parcelable