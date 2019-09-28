package me.li2.movies.data.remote

import me.li2.movies.App
import me.li2.movies.BuildConfig
import me.li2.movies.ui.movies.MoviesType
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class RemoteDataSource : KodeinAware {

    override val kodein by kodein(App.context)
    private val api: Api by instance(arg = Pair(TIMEOUT, BuildConfig.DEBUG))

    fun getMoviesAsync(type: MoviesType) = api.getMoviesAsync(type.value)

    companion object {
        const val TIMEOUT = 6000L
    }
}
