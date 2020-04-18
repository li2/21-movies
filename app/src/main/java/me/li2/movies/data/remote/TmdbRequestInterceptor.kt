package me.li2.movies.data.remote

import me.li2.android.network.interceptor.RequestInterceptor
import me.li2.movies.util.Constants
import okhttp3.HttpUrl
import okhttp3.Request

class TmdbRequestInterceptor : RequestInterceptor() {

    override var urlBuilder: HttpUrl.Builder.() -> Unit = {
        addQueryParameter(QUERY_PARAMETER_API_KEY, Constants.TMDB_API_KEY)
    }

    override var requestBuilder: Request.Builder.() -> Unit = {

    }

    companion object {
        private const val QUERY_PARAMETER_API_KEY = "api_key"
    }
}