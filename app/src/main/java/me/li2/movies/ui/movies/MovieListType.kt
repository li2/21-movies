/*
 * Created by Weiyi Li on 2020-05-31.
 * https://github.com/li2
 */
package me.li2.movies.ui.movies

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class MovieListType(val label: String): Parcelable

@Parcelize
object NowPlayingMovieList : MovieListType("Now Playing")

@Parcelize
object UpcomingMovieList : MovieListType("Upcoming")

@Parcelize
object PopularMovieList : MovieListType("Popular")

@Parcelize
object TopRatedMovieList : MovieListType("TopRated")

@Parcelize
data class RecMovieList(val movieId: Int) : MovieListType("Recommendations")

@Parcelize
data class GenreMovieList(val genre: String) : MovieListType(genre)

@Parcelize
data class SearchMovieList(val query: String) : MovieListType("")