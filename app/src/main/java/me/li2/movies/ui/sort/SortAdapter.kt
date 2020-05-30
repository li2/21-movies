/*
 * Created by Weiyi Li on 2020-05-30.
 * https://github.com/li2
 */
package me.li2.movies.ui.sort

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.jakewharton.rxbinding4.view.clicks
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.databinding.SortItemViewBinding

class SortAdapter(private val onSortChange: (MoviesSortType, Boolean) -> Unit)
    : ListAdapter<SortItemUI, SortViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortViewHolder {
        return SortViewHolder.create(parent, onSortClick)
    }

    override fun onBindViewHolder(holder: SortViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    private val onSortClick: (MoviesSortType) -> Unit = { currentSortType ->
        val currentMutableList = currentList.toMutableList()
        val currentCheckedIndex = currentList.indexOfFirst { it.sortType == currentSortType }
        val currentCheckedItem = currentList[currentCheckedIndex]
        val lastCheckedIndex = currentList.indexOfFirst { it.checked }
        val lastCheckedItem = currentList[lastCheckedIndex]

        if (currentCheckedIndex == lastCheckedIndex) {
            currentMutableList[currentCheckedIndex] = currentCheckedItem.copy(descending = !currentCheckedItem.descending)
        } else {
            currentMutableList[currentCheckedIndex] = currentCheckedItem.copy(checked = true)
            currentMutableList[lastCheckedIndex] = lastCheckedItem.copy(checked = false)
        }

        submitList(currentMutableList)

        currentMutableList[currentCheckedIndex].let {
            onSortChange(it.sortType, it.descending)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SortItemUI>() {
            override fun areItemsTheSame(oldItem: SortItemUI, newItem: SortItemUI) =
                    oldItem.sortType == newItem.sortType

            override fun areContentsTheSame(oldItem: SortItemUI, newItem: SortItemUI) =
                    oldItem == newItem
        }
    }
}

class SortViewHolder(binding: SortItemViewBinding,
                     private val onSortClick: (MoviesSortType) -> Unit)
    : BaseViewHolder<SortItemUI, SortItemViewBinding>(binding) {

    override fun bind(item: SortItemUI, position: Int) {
        binding.item = item
        binding.root.clicks().throttleFirstShort().subscribe {
            onSortClick(item.sortType)
        }
    }

    companion object {
        fun create(parent: ViewGroup,
                   onSortClick: (MoviesSortType) -> Unit): SortViewHolder {
            val binding = newBindingInstance(parent, R.layout.sort_item_view) as SortItemViewBinding
            return SortViewHolder(binding, onSortClick)
        }
    }
}

data class SortItemUI(val checked: Boolean,
                      val sortType: MoviesSortType,
                      val descending: Boolean)
