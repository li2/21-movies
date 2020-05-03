package me.li2.movies.di

import me.li2.movies.data.local.DBDataSource
import me.li2.movies.data.remote.TmdbDataSource
import me.li2.movies.data.repository.Repository
import me.li2.movies.util.Constants
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object MainComponent {

    val appModule = Kodein.Module("app module") {
        bind<Constants>() with singleton { Constants(instance()) }
        bind<Repository>() with provider { Repository() }
        bind<TmdbDataSource>() with provider { TmdbDataSource() }
        bind<DBDataSource>() with provider { DBDataSource() }
    }
}
