/*
 * Created by Weiyi on 2020-05-26.
 * https://github.com/li2
 */
package me.li2.movies.ui.filter

import androidx.annotation.WorkerThread
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.ui.filter.ReleaseDateFilter.*
import org.threeten.bp.LocalDate

class MoviesFilter {

    var queryText: String? = null
    var voteRange = IntRange(0, 10)
    var releaseDateFilter = ALL

    @WorkerThread
    fun performFiltering(movies: List<MovieItemUI>): List<MovieItemUI> {
        return movies.filter { movie ->
            containsQueryText(movie)
                    && inVoteRange(movie)
                    && isLatestReleased(movie)
        }
    }

    private fun containsQueryText(movie: MovieItemUI): Boolean {
        if (queryText.isNullOrEmpty()) return true
        return movie.title.contains(queryText.orEmpty(), true)
                || movie.overview.contains(queryText.orEmpty(), true)
    }

    private fun inVoteRange(movie: MovieItemUI): Boolean {
        return movie.voteAverage.let {
            it >= voteRange.first && it <= voteRange.last
        }
    }

    private fun isLatestReleased(movie: MovieItemUI): Boolean {
        if (movie.releaseDate == null) return true

        val now = LocalDate.now()
        return when (releaseDateFilter) {
            ALL -> true
            WITHIN_1_MONTH -> movie.releaseDate.isAfter(now.minusMonths(1))
            WITHIN_3_MONTHS -> movie.releaseDate.isAfter(now.minusMonths(3))
            WITHIN_1_YEAR -> movie.releaseDate.isAfter(now.minusMonths(12))
        }
    }
}