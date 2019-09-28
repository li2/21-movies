package me.li2.movies.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import me.li2.movies.data.remote.Api
import me.li2.movies.data.remote.StatusCodeInterceptor
import me.li2.movies.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.multiton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = Kodein.Module("network module") {

    bind<Api>() with multiton { timeoutAndDebug: Pair<Long, Boolean> ->
        Network.getRetrofitAdapter(Constants.BASE_URL, timeoutAndDebug.first, timeoutAndDebug.second)
    }
}

object Network {
    fun getRetrofitAdapter(baseUrl: String, timeout: Long, debug: Boolean): Api =
            Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(getMoshiConverterFactory())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient(timeout, debug))
                    .build()
                    .create(Api::class.java)

    private fun getOkHttpClient(timeout: Long, debug: Boolean): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(StatusCodeInterceptor())
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(if (debug) BODY else NONE))
                    .build()

    private fun getMoshiConverterFactory(): MoshiConverterFactory =
            MoshiConverterFactory.create(Moshi.Builder()
                    .add(Wrapped.ADAPTER_FACTORY)
                    .add(KotlinJsonAdapterFactory())
                    .build())
}
