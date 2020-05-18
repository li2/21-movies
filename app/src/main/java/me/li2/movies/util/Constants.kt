/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.util

import android.content.res.Resources
import me.li2.movies.R

class Constants(private val res: Resources) {
    companion object {
        lateinit var TMDB_URL: String
        lateinit var TMDB_API_KEY: String
    }

    fun init() {
        TMDB_URL = res.getString(R.string.tmdbUrl)
        TMDB_API_KEY = res.getString(R.string.tmdb_api_key)
    }
}
