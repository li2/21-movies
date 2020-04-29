/*
 * Copyright 2020 The Android Open Source Project
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

package me.li2.movies.ui.widgets.paging

/**
 * LoadState of a list load
 */
sealed class PagingState {
    /**
     * Loading is in progress.
     */
    object Loading : PagingState()

    /**
     * Loading is complete.
     */
    data class Done(val page: Int = 0, val totalPages: Int = Int.MAX_VALUE) : PagingState()

    /**
     * Loading hit an error.
     *
     * @param error [Throwable] that caused the load operation to generate this error state.
     *
     */
    data class Error(val error: String) : PagingState()
}
