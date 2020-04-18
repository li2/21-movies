package me.li2.movies.di

import me.li2.android.network.NetworkBuilder
import me.li2.android.network.connectivity.NetworkMonitor
import me.li2.movies.BuildConfig
import me.li2.movies.data.remote.AbcApi
import me.li2.movies.data.remote.AbcStatusInterceptor
import me.li2.movies.data.remote.TmdbApi
import me.li2.movies.data.remote.TmdbRequestInterceptor
import me.li2.movies.util.Constants
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.multiton
import org.kodein.di.generic.singleton

val networkModule = Kodein.Module("network module") {

    bind<NetworkMonitor>() with singleton { NetworkMonitor(instance()) }

    bind<AbcApi>() with multiton { timeout: Long ->
        NetworkBuilder.buildRetrofitAdapter<AbcApi>(
                context = instance(),
                baseUrl = Constants.BASE_URL,
                interceptors = listOf(AbcStatusInterceptor(instance())),
                timeout = timeout,
                debug = BuildConfig.DEBUG)
    }

    bind<TmdbApi>() with singleton {
        NetworkBuilder.buildRetrofitAdapter<TmdbApi>(
                context = instance(),
                baseUrl = Constants.TMDB_URL,
                interceptors = listOf(TmdbRequestInterceptor()),
                debug = BuildConfig.DEBUG)
    }
}
