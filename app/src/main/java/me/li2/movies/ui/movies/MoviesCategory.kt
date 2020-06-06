/*
 * Created by Weiyi Li on 2020-05-31.
 * https://github.com/li2
 */
package me.li2.movies.ui.movies

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import me.li2.movies.data.model.GenreUI
import me.li2.movies.data.model.TimeWindow

sealed class MoviesCategory(val label: String): Parcelable

@Parcelize
data class TrendingCategory(val timeWindow: TimeWindow): MoviesCategory("Trending ${timeWindow.value}")

@Parcelize
object NowPlayingCategory : MoviesCategory("Now Playing")

@Parcelize
object UpcomingCategory : MoviesCategory("Upcoming")

@Parcelize
object PopularCategory : MoviesCategory("Popular")

@Parcelize
object TopRatedCategory : MoviesCategory("Top Rated")

@Parcelize
data class RecommendationCategory(val movieId: Int) : MoviesCategory("Recommendations")

@Parcelize
data class GenreCategory(val genre: GenreUI) : MoviesCategory(genre.name)

@Parcelize
data class QueryCategory(val query: String) : MoviesCategory("")

@Parcelize
object Watchlist: MoviesCategory("Watchlist")