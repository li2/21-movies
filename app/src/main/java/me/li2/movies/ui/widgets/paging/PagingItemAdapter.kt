/*
 * Copyright (C) 2020 The Android Open Source Project
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

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class PagingItemAdapter : RecyclerView.Adapter<PagingItemViewHolder>() {

    private val retryPublish: PublishSubject<Unit> = PublishSubject.create()
    val retryClicks: Observable<Unit> = retryPublish.toFlowable(BackpressureStrategy.LATEST).toObservable()

    /**
     * PagingState to present in the adapter.
     *
     * Changing this property will immediately notify the Adapter to change the item it's
     * presenting.
     */
    // 21note: notifyItemChanged() make the RecyclerView scroll and jump to UP,
    // use notifyDataSetChanged() and getItemCount always return 1 to resolve this issue.
    // https://stackoverflow.com/q/36724898/2722270
    var pagingState: PagingState = PagingState.Done()
        set(value) {
            if (field != value) {
//                val displayOldItem = displayLoadStateAsItem(field)
//                val displayNewItem = displayLoadStateAsItem(value)
//                if (displayOldItem && !displayNewItem) {
//                    notifyItemRemoved(0)
//                } else if (displayNewItem && !displayOldItem) {
//                    notifyItemInserted(0)
//                } else if (displayOldItem && displayNewItem) {
//                    notifyItemChanged(0)
//                }
                notifyDataSetChanged()
                field = value
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingItemViewHolder {
        return PagingItemViewHolder.create(parent, retryPublish)
    }

    override fun onBindViewHolder(holder: PagingItemViewHolder, position: Int) {
        holder.bind(pagingState, position)
    }

//    override fun getItemCount(): Int = if (displayLoadStateAsItem(pagingState)) 1 else 0
    override fun getItemCount(): Int = 1

    /**
     * Returns true if the PagingState should be displayed as a list item when active.
     * - loading
     * - error
     * - last page
     */
    private fun displayLoadStateAsItem(pagingState: PagingState): Boolean {
        return pagingState is PagingState.Loading
                || pagingState is PagingState.Error
                || (pagingState is PagingState.Done && pagingState.page == pagingState.totalPages)
    }
}