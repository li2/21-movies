package me.li2.movies.data.remote

import android.net.ConnectivityManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import me.li2.movies.App
import me.li2.movies.data.model.ResponseAPI
import me.li2.movies.util.orFalse
import okhttp3.Interceptor
import okhttp3.Response
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import java.io.IOException
import java.net.HttpURLConnection.HTTP_OK

open class StatusCodeInterceptor : Interceptor, KodeinAware {

    override val kodein: Kodein by kodein(App.context)
    override val kodeinContext = kcontext(App.context)
    private val connectivityManager: ConnectivityManager by instance()
    private val isConnected
        get() = connectivityManager.activeNetworkInfo?.isConnected.orFalse()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected) {
            throw NoNetworkException
        }
        val request = chain.request()
        val response = chain.proceed(request)
        val statusCode = response.code()
        if (statusCode < HTTP_OK || statusCode > 299 || response.body() == null) {
            handleErrorResponse(response)
        }
        return response
    }

    open fun handleErrorResponse(response: Response?) {
        response?.body()?.let {
            val json = it.string()
            val moshi = Moshi.Builder().build()
            val adapter: JsonAdapter<ResponseAPI> = moshi.adapter(Types.newParameterizedType(ResponseAPI::class.java))
            val errorAPI = adapter.fromJson(json)
            errorAPI?.let {
                throw ApiException(response.code(), errorAPI.error?.message ?: response.message())
            }
        }
    }
}

data class ApiException(val statusCode: Int, val errorMessage: String) : IOException(errorMessage)

object NoNetworkException: IOException("No network available, please check your WiFi or Data connection")