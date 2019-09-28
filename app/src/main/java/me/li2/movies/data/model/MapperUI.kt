package me.li2.movies.data.model

import me.li2.movies.ui.movies.MovieItem

object MapperUI {

    fun toMovieUI(api: MovieAPI) = MovieItem(
            id = api.id,
            name = api.name,
            runningTime = api.runningTime,
            type = api.type,
            description = api.description,
            rate = api.rate,
            trailerUrl = api.trailerUrl)
}