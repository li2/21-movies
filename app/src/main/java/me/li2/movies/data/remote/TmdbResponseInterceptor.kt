/*
 * Created by Weiyi Li on 2020-04-17.
 * https://github.com/li2
 */
package me.li2.movies.data.remote

import me.li2.android.network.connectivity.NetworkConnectivityListener
import me.li2.android.network.interceptor.ResponseInterceptor
import me.li2.android.network.json.SerializationUtils
import me.li2.movies.data.model.ApiException
import me.li2.movies.data.model.TmdbErrorAPI
import me.li2.movies.util.reportException
import okhttp3.Response

class TmdbResponseInterceptor(nc: NetworkConnectivityListener) : ResponseInterceptor(nc) {

    override fun handleErrorResponse(response: Response) {
        response.body?.string()?.let { json ->
            SerializationUtils.fromJson<TmdbErrorAPI>(json)?.let { errorApi ->
                ApiException(response.code, errorApi.errorCode, errorApi.errorMessage).let {
                    reportException(it)
                    throw it
                }
            }
        }
    }
}
