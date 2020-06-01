/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.li2.movies.util

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import androidx.appcompat.app.AppCompatDelegate.*

object ThemeHelper {
    const val LIGHT_MODE = "Light"
    const val DARK_MODE = "Dark"
    const val DEFAULT_MODE = "System default"

    fun applyTheme(themePref: String) {
        when (themePref) {
            LIGHT_MODE -> MODE_NIGHT_NO
            DARK_MODE -> MODE_NIGHT_YES
            else -> {
                if (SDK_INT >= Q) MODE_NIGHT_FOLLOW_SYSTEM else MODE_NIGHT_AUTO_BATTERY
            }
        }.let { mode ->
            setDefaultNightMode(mode)
        }
    }
}