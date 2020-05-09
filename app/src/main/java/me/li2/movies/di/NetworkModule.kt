package me.li2.movies.di

import me.li2.android.network.NetworkBuilder
import me.li2.android.network.connectivity.NetworkMonitor
import me.li2.movies.AppBuildConfig
import me.li2.movies.data.remote.TmdbApi
import me.li2.movies.data.remote.TmdbRequestInterceptor
import me.li2.movies.data.remote.TmdbResponseInterceptor
import me.li2.movies.util.Constants
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val networkModule = Kodein.Module("network module") {

    bind<NetworkMonitor>() with singleton { NetworkMonitor(instance()) }

    bind<TmdbApi>() with singleton {
        NetworkBuilder.buildRetrofitAdapter<TmdbApi>(
                context = instance(),
                baseUrl = Constants.TMDB_URL,
                interceptors = listOf(
                        TmdbRequestInterceptor(),
                        TmdbResponseInterceptor(instance())),
                timeout = TmdbApi.TIMEOUT,
                debug = AppBuildConfig.DEBUG)
    }
}
