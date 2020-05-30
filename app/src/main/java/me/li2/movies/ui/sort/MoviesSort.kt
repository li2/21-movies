/*
 * Created by Weiyi on 2020-05-30.
 * https://github.com/li2
 */
package me.li2.movies.ui.sort

import androidx.annotation.WorkerThread
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.ui.sort.MoviesSortType.*

class MoviesSort {

    var sortType = RATING
    var descending = true

    @WorkerThread
    fun performSort(movies: List<MovieItemUI>): List<MovieItemUI> {
        return when (sortType) {
            ALPHABETICAL -> movies.sortedBy { it.title }
            POPULARITY -> movies.sortedBy { it.popularity }
            RATING -> movies.sortedBy { it.voteAverage }
            RELEASE_DATE -> movies.sortedBy { it.releaseDate }
        }.let {
            if (descending) it.reversed() else it
        }
    }

    fun buildSortItems(): List<SortItemUI> {
        return listOf(ALPHABETICAL, POPULARITY, RATING, RELEASE_DATE).map {
            SortItemUI(checked = it == sortType,
                    sortType = it,
                    descending = if (it == sortType) descending else false)
        }
    }
}
