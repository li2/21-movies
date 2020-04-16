package me.li2.movies.data.remote

import me.li2.android.network.connectivity.NetworkConnectivityListener
import me.li2.android.network.interceptor.ResponseInterceptor
import me.li2.android.network.json.SerializationUtils
import me.li2.movies.data.model.ResponseAPI
import okhttp3.Response

class AbcStatusInterceptor(nc: NetworkConnectivityListener) : ResponseInterceptor(nc) {

    override fun handleErrorResponse(response: Response) {
        response.body?.string()?.let { json ->
            SerializationUtils.fromJson<ResponseAPI>(json)?.run {
                throw ApiException(response.code, error?.message ?: response.message)
            }
        }
    }
}
