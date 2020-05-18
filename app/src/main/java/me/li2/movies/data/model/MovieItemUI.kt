/*
 * Created by Weiyi Li on 2020-04-20.
 * https://github.com/li2
 */
package me.li2.movies.data.model

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import kotlinx.android.parcel.Parcelize
import me.li2.android.common.arch.Resource
import me.li2.android.common.logic.orFalse
import me.li2.movies.data.remote.TmdbApi
import org.threeten.bp.LocalDate

@Parcelize
data class MovieItemPagingUI(
        val results: List<MovieItemUI>,
        val page: Int,
        val totalPages: Int,
        val totalResults: Int
) : Parcelable

@Parcelize
data class MovieItemUI(
        val id: Int,
        val title: String,
        val releaseDate: LocalDate?,
        val releaseDateDisplay: String,
        val posterUrl: String?,
        val backdropUrl: String?,
        val popularity: Double,
        val voteAverage: Double,
        val voteAverageDisplay: String,
        val voteCount: Int,
        val voteCountDisplay: String,
        val overview: String
) : Parcelable

fun MutableLiveData<Resource<MovieItemPagingUI>>.nextPage(): Int {
    return this.value?.data?.let { it.page + 1 }
            ?: TmdbApi.TMDB_STARTING_PAGE_INDEX
}

fun MutableLiveData<Resource<MovieItemPagingUI>>.isLastPage(): Boolean {
    return this.value?.data?.let { it.page == it.totalPages }.orFalse()
}

fun MutableLiveData<Resource<MovieItemPagingUI>>.appendResults(results: List<MovieItemUI>): List<MovieItemUI> {
    return this.value?.data?.results.orEmpty().toMutableList()
            .let {
                it.addAll(results)
                // remove duplicated items, 21note
                it.distinctBy { movie -> movie.id }
            }
}

fun Resource<List<MovieItemUI>>.noData() = this.data?.isNullOrEmpty().orFalse()