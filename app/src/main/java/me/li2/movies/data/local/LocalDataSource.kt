package me.li2.movies.data.local

import me.li2.movies.App
import me.li2.movies.data.model.MovieDetailUI
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LocalDataSource : KodeinAware {
    override val kodein by kodein(App.context)
    private val db by instance<AppDatabase>()

    suspend fun saveMovieDetail(movieDetailUI: MovieDetailUI): Long =
            db.movieDao().saveMovieDetail(movieDetailUI)

    suspend fun getMovieDetail(movieId: Int): MovieDetailUI? =
            db.movieDao().getMovieDetail(movieId)
}
