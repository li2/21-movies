/*
 * Created by Weiyi Li on 2019-09-29.
 * https://github.com/li2
 */
package me.li2.movies.data.model

import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.*
import me.li2.android.common.number.orZero
import me.li2.movies.data.remote.TmdbApi
import me.li2.movies.fcm.MessageAPI
import me.li2.movies.ui.widgets.paging.PagingState
import me.li2.movies.util.NumberFormatter.formatRuntime
import me.li2.movies.util.NumberFormatter.formatVoteCount
import me.li2.movies.util.parseLocalDate
import me.li2.movies.util.releaseDateWithBestLocalPattern

object MapperUI {

    fun toMovieItemUI(api: TmdbMovieAPI) = MovieItemUI(
            id = api.id,
            title = api.title,
            releaseDate = api.releaseDate?.parseLocalDate(),
            releaseDateDisplay = api.releaseDate?.releaseDateWithBestLocalPattern().orEmpty(),
            posterUrl = TmdbApi.imageW500Url(api.posterPath),
            backdropUrl = TmdbApi.imageOriginalUrl(api.backdropPath),
            popularity = api.popularity,
            voteAverage = api.voteAverage,
            voteAverageDisplay = api.voteAverage.toString(),
            voteCount = api.voteCount,
            voteCountDisplay = formatVoteCount(api.voteCount),
            overview = api.overview)

    fun toMovieItemUI(api: MessageAPI) = MovieItemUI(
            id = api.id.toInt(),
            title = api.title,
            releaseDate = api.releaseDate.parseLocalDate(),
            releaseDateDisplay = api.releaseDate.releaseDateWithBestLocalPattern(),
            posterUrl = api.posterUrl,
            backdropUrl = "",
            popularity = 0.0,
            voteAverage = 0.0,
            voteAverageDisplay = "0.0",
            voteCount = 0,
            voteCountDisplay = "0",
            overview = api.overview)

    fun toMovieItemPagingUI(api: TmdbMovieListAPI) = MovieItemPagingUI(
            results = api.results.map { toMovieItemUI(it) },
            page = api.page,
            totalPages = api.totalPages,
            totalResults = api.totalResults)

    private fun toGenreUI(api: Genre) = GenreUI(api.id, api.name)

    fun toMovieDetailUI(api: TmdbMovieDetailAPI) = MovieDetailUI(
            id = api.id,
            title = api.title,
            overview = api.overview.orEmpty(),
            tagline = api.tagline.orEmpty(),
            releaseDate = api.releaseDate.parseLocalDate(),
            releaseDateDisplay = api.releaseDate.releaseDateWithBestLocalPattern(),
            runtime = formatRuntime(api.runtime),
            genres = api.genres.map { toGenreUI(it) },
            productionCountry = api.productionCountries.firstOrNull()?.name.orEmpty(),
            originalLanguage = api.originalLanguage,
            spokenLanguages = api.spokenLanguages.joinToString(separator = ", ") { it.name },
            imdbUrl = TmdbApi.imdbUrl(api.imdbId),
            posterUrl = TmdbApi.imageW500Url(api.posterPath),
            backdropUrl = TmdbApi.imageOriginalUrl(api.backdropPath),
            popularity = api.popularity,
            voteAverage = api.voteAverage,
            voteAverageDisplay = api.voteAverage.toString(),
            voteCount = api.voteCount,
            voteCountDisplay = formatVoteCount(api.voteCount))

    fun toMovieDetailUI(item: MovieItemUI) = MovieDetailUI(
            id = item.id,
            title = item.title,
            overview = item.overview,
            tagline = "",
            releaseDate = item.releaseDate,
            releaseDateDisplay = item.releaseDateDisplay,
            runtime = "",
            genres = emptyList(),
            productionCountry = "",
            originalLanguage = "",
            spokenLanguages = "",
            imdbUrl = null,
            posterUrl = item.posterUrl,
            backdropUrl = item.backdropUrl,
            popularity = 0.0,
            voteAverage = item.voteAverage,
            voteAverageDisplay = item.voteAverageDisplay,
            voteCount = 0,
            voteCountDisplay = "0")

    fun toMovieReviewsUI(api: TmdbMovieReviewListAPI) = MovieReviewListUI(
            reviews = api.results.map {
                MovieReviewUI(id = it.id,
                        movieId = api.movieId,
                        author = it.author,
                        content = it.content)
            },
            page = api.page,
            totalPages = api.totalPages,
            totalResults = api.totalResults)

    fun toTrailersUI(api: TmdbMovieVideoListAPI): List<Trailer> {
        val youtubeTrailersAPI = api.results.filter { it.site.equals("youtube", true) }
        return youtubeTrailersAPI.mapNotNull { trailerAPI ->
            val url = TmdbApi.youtubeTrailerUrl(trailerAPI.path)
            val thumbnailUrl = TmdbApi.youtubeTrailerThumbnailUrl(trailerAPI.path)
            if (url != null && thumbnailUrl != null) {
                Trailer(trailerAPI.id, api.movieId, url, thumbnailUrl)
            } else {
                null
            }
        }
    }

    private fun toCastUI(api: CastAPI) = CastUI(
            castId = api.castId,
            characterOrJob = api.character,
            creditId = api.creditId,
            gender = api.gender,
            id = api.id,
            name = api.name,
            order = api.order,
            profileUrl = TmdbApi.imageW500Url(api.profilePath))

    private fun toCrewUI(api: CrewAPI) = CrewUI(
            creditId = api.creditId,
            department = api.department,
            gender = api.gender,
            id = api.id,
            characterOrJob = api.job,
            name = api.name,
            profileUrl = TmdbApi.imageW500Url(api.profilePath))

    fun toCreditListUI(api: TmdbMovieCreditListAPI) = CreditListUI(
            movieId = api.id,
            casts = api.casts.map { toCastUI(it) }.distinctBy { it.id }.sortedBy { it.order },
            crews = api.crews.map { toCrewUI(it) }.distinctBy { it.id }
    )

    fun toPagingState(resource: Resource<MovieItemPagingUI>): PagingState {
        return when (resource.status) {
            LOADING -> PagingState.Loading
            SUCCESS -> PagingState.Done(resource.data?.page.orZero(), resource.data?.totalPages.orZero())
            ERROR -> PagingState.Error("\uD83D\uDE28 Wooops ${resource.exception?.message.orEmpty()}")
        }
    }
}