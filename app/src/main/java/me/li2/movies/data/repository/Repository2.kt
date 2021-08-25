/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.data.repository

import me.li2.movies.data.model.GenreUI
import me.li2.movies.data.model.MapperUI
import me.li2.movies.data.remote.TmdbDataSource

class Repository2(private val tmdbDataSource: TmdbDataSource) {

    suspend fun getGenresAsync(): List<GenreUI> {
        return tmdbDataSource.getGenres().genres.map { MapperUI.toGenreUI(it) }
    }
}
