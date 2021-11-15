package me.li2.movies.di

import android.content.Context
import dagger.Module
import dagger.Provides
import me.li2.android.network.NetworkBuilder
import me.li2.android.network.connectivity.NetworkMonitor
import me.li2.movies.AppBuildConfig
import me.li2.movies.data.remote.TmdbApi
import me.li2.movies.data.remote.TmdbRequestInterceptor
import me.li2.movies.data.remote.TmdbResponseInterceptor
import me.li2.movies.util.Constants
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideTmdbApi(@ApplicationContext context: Context): TmdbApi {
        return NetworkBuilder.buildRetrofitAdapter(
            context = context,
            baseUrl = Constants.TMDB_URL,
            interceptors = listOf(
                TmdbRequestInterceptor(),
                TmdbResponseInterceptor(NetworkMonitor(context))
            ),
            timeout = TmdbApi.TIMEOUT,
            debug = AppBuildConfig.DEBUG
        )
    }
}
