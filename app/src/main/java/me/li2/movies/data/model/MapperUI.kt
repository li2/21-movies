package me.li2.movies.data.model

import android.net.Uri
import me.li2.movies.data.remote.TmdbApi
import me.li2.movies.ui.home.top.TopItemUI
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


    fun toTopItemUI(api: TmdbMovieAPI) = TopItemUI(
            id = api.id,
            title = api.title,
            releaseDate = api.releaseDate,
            posterUrl = TmdbApi.imageUrl(api.posterPath),
            voteAverage = api.voteAverage,
            voteCount = api.voteCount,
            overview = api.overview)
}