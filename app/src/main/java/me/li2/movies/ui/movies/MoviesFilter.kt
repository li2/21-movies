/*
 * Created by Weiyi on 2020-05-26.
 * https://github.com/li2
 */
package me.li2.movies.ui.movies

import androidx.annotation.WorkerThread
import me.li2.movies.data.model.MovieItemUI

class MoviesFilter {

    var queryText: String? = null

    @WorkerThread
    fun performFiltering(movies: List<MovieItemUI>): List<MovieItemUI> {
        return if (!needFilter()) {
            movies
        } else {
            movies.filter { movie ->
                containsQueryText(movie, queryText)
            }
        }
    }

    private fun needFilter(): Boolean {
        return !queryText.isNullOrEmpty()
    }

    private fun containsQueryText(movie: MovieItemUI, queryText: String?): Boolean {
        if (queryText.isNullOrEmpty()) return true
        return movie.title.contains(queryText, true)
                || movie.overview.contains(queryText, true)
    }
}