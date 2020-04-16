package me.li2.movies.di

import me.li2.android.network.NetworkBuilder
import me.li2.android.network.connectivity.NetworkMonitor
import me.li2.movies.data.remote.AbcApi
import me.li2.movies.data.remote.AbcStatusInterceptor
import me.li2.movies.util.Constants
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.multiton
import org.kodein.di.generic.singleton

val networkModule = Kodein.Module("network module") {

    bind<NetworkMonitor>() with singleton { NetworkMonitor(instance()) }

    bind<AbcStatusInterceptor>() with singleton { AbcStatusInterceptor(instance()) }

    bind<AbcApi>() with multiton { timeout: Long ->
        NetworkBuilder.buildRetrofitAdapter<AbcApi>(instance(), Constants.BASE_URL, instance(), timeout = timeout)
    }
}
