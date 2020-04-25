package me.li2.movies.data.model

import android.net.Uri
import me.li2.android.common.number.orZero
import me.li2.movies.data.remote.TmdbApi
import me.li2.movies.ui.home.MovieItemUI
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
            posterUrl = TmdbApi.imageUrl(api.posterPath),
            voteAverage = api.voteAverage,
            voteCount = api.voteCount,
            overview = api.overview)

    fun toMovieDetailUI(api: TmdbMovieDetailAPI) = MovieDetailUI(
            id = api.id,
            title = api.title,
            overview = api.overview.orEmpty(),
            tagline = api.tagline.orEmpty(),
            releaseDate = api.releaseDate,
            runtime = api.runtime.orZero(),
            originalLanguage = api.originalLanguage,
            spokenLanguages = api.spokenLanguages.map { it.name },
            posterUrl = TmdbApi.imageUrl(api.posterPath),
            backdropUrl = TmdbApi.imageUrl(api.backdropPath),
            popularity = api.popularity,
            voteAverage = api.voteAverage,
            voteCount = api.voteCount)

    fun toMovieReviewsUI(api: TmdbMovieReviewListAPI) = api.results.map {
        MovieReviewUI(author = it.author, content = it.content)
    }

    fun toYoutubeUrl(api: TmdbMovieVideoListAPI): String? {
        val key = api.results.firstOrNull { it.site.equals("youtube", true) }?.key
        return TmdbApi.youtubeTrailerUrl(key)
    }
}