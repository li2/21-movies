package me.li2.movies.data.model

import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.*
import me.li2.android.common.number.orZero
import me.li2.movies.data.remote.TmdbApi
import me.li2.movies.fcm.MessageAPI
import me.li2.movies.ui.widgets.paging.PagingState
import me.li2.movies.util.NumberFormatter.formatRuntime
import me.li2.movies.util.NumberFormatter.formatVoteCount

object MapperUI {

    fun toMovieItemUI(api: TmdbMovieAPI) = MovieItemUI(
            id = api.id,
            title = api.title,
            releaseDate = api.releaseDate.orEmpty(),
            posterUrl = TmdbApi.imageW500Url(api.posterPath),
            backdropUrl = TmdbApi.imageOriginalUrl(api.backdropPath),
            voteAverage = api.voteAverage.toString(),
            voteCount = formatVoteCount(api.voteCount),
            overview = api.overview)

    fun toMovieItemUI(api: MessageAPI) = MovieItemUI(
            id = api.id.toInt(),
            title = api.title,
            releaseDate = api.releaseDate,
            posterUrl = api.posterUrl,
            backdropUrl = "",
            voteAverage = "",
            voteCount = "",
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
            releaseDate = api.releaseDate,
            runtime = formatRuntime(api.runtime),
            genres = api.genres.map { toGenreUI(it) },
            productionCountry = api.productionCountries.firstOrNull()?.name.orEmpty(),
            originalLanguage = api.originalLanguage,
            spokenLanguages = api.spokenLanguages.joinToString(separator = ", ") { it.name },
            imdbUrl = TmdbApi.imdbUrl(api.imdbId),
            posterUrl = TmdbApi.imageW500Url(api.posterPath),
            backdropUrl = TmdbApi.imageOriginalUrl(api.backdropPath),
            youtubeTrailerUrl = null,
            popularity = api.popularity.toString(),
            voteAverage = api.voteAverage.toString(),
            voteCount = formatVoteCount(api.voteCount))

    fun toMovieDetailUI(item: MovieItemUI) = MovieDetailUI(
            id = item.id,
            title = item.title,
            overview = item.overview,
            tagline = "",
            releaseDate = item.releaseDate,
            runtime = "",
            genres = emptyList(),
            productionCountry = "",
            originalLanguage = "",
            spokenLanguages = "",
            imdbUrl = null,
            posterUrl = item.posterUrl,
            backdropUrl = item.backdropUrl,
            youtubeTrailerUrl = null,
            popularity = "",
            voteAverage = item.voteAverage,
            voteCount = "")

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

    fun toTrailer(api: TmdbMovieVideoListAPI): Trailer? {
        val youtubeApi = api.results.firstOrNull { it.site.equals("youtube", true) }
        return youtubeApi?.let {
            TmdbApi.youtubeTrailerUrl(it.path)?.let { url ->
                Trailer(id = it.id, movieId = api.movieId, url = url)
            }
        }
    }

    fun toPagingState(resource: Resource<MovieItemPagingUI>): PagingState {
        return when (resource.status) {
            LOADING -> PagingState.Loading
            SUCCESS -> PagingState.Done(resource.data?.page.orZero(), resource.data?.totalPages.orZero())
            ERROR -> PagingState.Error("\uD83D\uDE28 Wooops ${resource.exception?.message.orEmpty()}")
        }
    }
}