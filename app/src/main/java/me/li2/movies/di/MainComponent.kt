package me.li2.movies.di

import me.li2.movies.data.local.AppDatabase
import me.li2.movies.data.local.LocalDataSource
import me.li2.movies.data.remote.TmdbDataSource
import me.li2.movies.data.repository.Repository
import me.li2.movies.util.Constants
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object MainComponent {

    val appModule = Kodein.Module("app module") {
        bind<Constants>() with singleton { Constants(instance()) }
        bind<Repository>() with singleton { Repository() }
        bind<TmdbDataSource>() with singleton { TmdbDataSource() }
        bind<AppDatabase>() with eagerSingleton { AppDatabase.buildRoomDatabase(instance()) }
        bind<LocalDataSource>() with singleton { LocalDataSource() }
    }
}
