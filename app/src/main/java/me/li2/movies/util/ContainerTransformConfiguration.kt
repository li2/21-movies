/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.li2.movies.util

import android.view.animation.Interpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import javax.inject.Inject

/** Holds the default configurations for the container transform in demos and catalog navigation.  */
class ContainerTransformConfiguration @Inject constructor() {
    val isArcMotionEnabled: Boolean
        get() = false

    val enterDuration: Long = 300

    val returnDuration: Long = 275

    val interpolator: Interpolator = FastOutSlowInInterpolator()
}