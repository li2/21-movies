/*
 * Created by Weiyi Li on 2020-06-07.
 * https://github.com/li2
 */
package me.li2.movies.data.repository

import android.content.SharedPreferences
import me.li2.movies.util.ThemeHelper

class AppSettings(private val sp: SharedPreferences) {

    var themePref: String
        get() = getStringPref(SP_KEY_THEME, ThemeHelper.DARK_MODE)
        set(value) = setStringPref(SP_KEY_THEME, value)

    private fun getStringPref(key: String, default: String): String {
        return sp.getString(key, default) ?: default
    }

    private fun setStringPref(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    companion object {
        const val SP_FILE_NAME = "me.li2.movies.settings"
        private const val SP_KEY_THEME = "theme"
    }
}