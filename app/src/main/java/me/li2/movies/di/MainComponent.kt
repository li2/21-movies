package me.li2.movies.di

import me.li2.movies.data.local.AppDatabase
import me.li2.movies.data.local.LocalDataSource
import me.li2.movies.data.remote.TmdbDataSource
import me.li2.movies.data.repository.Repository
import me.li2.movies.util.Constants
import org.kodein.di.Kodein
import org.kodein.di.generic.*

object MainComponent {

    val appModule = Kodein.Module("app module") {
        bind<Constants>() with singleton { Constants(instance()) }
        bind<Repository>() with provider { Repository() }
        bind<TmdbDataSource>() with provider { TmdbDataSource() }
        bind<AppDatabase>() with eagerSingleton { AppDatabase.buildRoomDatabase(instance()) }
        bind<LocalDataSource>() with provider { LocalDataSource() }
    }
}
