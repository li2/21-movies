package me.li2.movies.data.model

import android.net.Uri
import me.li2.android.common.number.NumberFormatUtils.formatNumber
import me.li2.android.common.number.orZero
import me.li2.movies.data.remote.TmdbApi
import me.li2.movies.ui.movies.MovieItem
import timber.log.Timber.e

object MapperUI {

    private fun urlToUri(url: String) = try {
        Uri.parse(url)
    } catch (exception: Exception) {
        e(exception, "Failed parse url to uri: $this")
        null
    }

    fun toMovieUI(api: MovieAPI) = MovieItem(
            id = api.id,
            name = api.name,
            runningTime = api.runningTime,
            type = api.type,
            description = api.description,
            rate = api.rate,
            trailerUri = urlToUri(api.trailerUrl))


    fun toMovieItemUI(api: TmdbMovieAPI) = MovieItemUI(
            id = api.id,
            title = api.title,
            releaseDate = api.releaseDate,
            posterUrl = TmdbApi.imageW500Url(api.posterPath),
            backdropUrl = TmdbApi.imageOriginalUrl(api.backdropPath),
            voteAverage = api.voteAverage.toString(),
            voteCount = formatNumber(api.voteCount.toDouble(), "###,###", ','),
            overview = api.overview)

    private fun toDisplayRuntime(runtime: Int?): String {
        val hours = runtime.orZero() / 60
        val minutes = runtime.orZero() % 60
        return when {
            hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
            hours > 0 && minutes == 0 -> "${hours}h"
            hours == 0 && minutes > 0 -> "${minutes}m"
            else -> null
        }.orEmpty()
    }

    private fun toGenreUI(api: Genre) = GenreUI(api.id, api.name)

    fun toMovieDetailUI(api: TmdbMovieDetailAPI) = MovieDetailUI(
            id = api.id,
            title = api.title,
            overview = api.overview.orEmpty(),
            tagline = api.tagline.orEmpty(),
            releaseDate = api.releaseDate,
            runtime = toDisplayRuntime(api.runtime),
            genres = api.genres.map { toGenreUI(it) },
            productionCountry = api.productionCountries.firstOrNull()?.name.orEmpty(),
            originalLanguage = api.originalLanguage,
            spokenLanguages = api.spokenLanguages.joinToString(separator = ", ") { it.name },
            imdbUrl = TmdbApi.imdbUrl(api.imdbId),
            posterUrl = TmdbApi.imageW500Url(api.posterPath),
            backdropUrl = TmdbApi.imageOriginalUrl(api.backdropPath),
            popularity = api.popularity,
            voteAverage = api.voteAverage,
            voteCount = api.voteCount)

    fun toMovieReviewsUI(api: TmdbMovieReviewListAPI) = MovieReviewListUI(
            reviews = api.results.map {
                MovieReviewUI(id = it.id, author = it.author, content = it.content)
            },
            page = api.page,
            totalPages = api.totalPages,
            totalResults = api.totalResults)

    fun toYoutubeUrl(api: TmdbMovieVideoListAPI): String? {
        val key = api.results.firstOrNull { it.site.equals("youtube", true) }?.key
        return TmdbApi.youtubeTrailerUrl(key)
    }
}