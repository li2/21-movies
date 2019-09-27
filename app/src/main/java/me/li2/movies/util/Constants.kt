package me.li2.movies.util

import android.content.res.Resources
import me.li2.movies.R

class Constants(private val res: Resources) {
    companion object {
        lateinit var BASE_URL: String
    }

    fun init() {
        BASE_URL = res.getString(R.string.baseUrl)
    }
}
