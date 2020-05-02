package me.li2.movies.data.remote

import me.li2.android.network.connectivity.NetworkConnectivityListener
import me.li2.android.network.interceptor.ResponseInterceptor
import me.li2.android.network.json.SerializationUtils
import me.li2.movies.data.model.ApiException
import me.li2.movies.data.model.TmdbErrorAPI
import okhttp3.Response

class TmdbResponseInterceptor(nc: NetworkConnectivityListener) : ResponseInterceptor(nc) {

    override fun handleErrorResponse(response: Response) {
        response.body?.string()?.let { json ->
            SerializationUtils.fromJson<TmdbErrorAPI>(json)?.let { errorApi ->
                throw ApiException(response.code, errorApi.errorCode, errorApi.errorMessage)
            }
        }
    }
}
