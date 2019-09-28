package me.li2.movies.ui.movies

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class MoviesTabType: Parcelable {
    NOT_SHOWING,
    COMING_SOON
}
