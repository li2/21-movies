/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.util

import android.content.Context
import me.li2.movies.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Constants @Inject constructor(
    private val context: Context
) {
    companion object {
        lateinit var TMDB_URL: String
        lateinit var TMDB_API_KEY: String
        lateinit var SOURCE_CODE_URL: String
        lateinit var PLAYSTORE_URL: String
    }

    fun init() {
        TMDB_URL = context.getString(R.string.tmdbUrl)
        TMDB_API_KEY = context.getString(R.string.tmdb_api_key)
        SOURCE_CODE_URL = "https://github.com/li2/21-movies"
        PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=me.li2.movies"
    }
}
