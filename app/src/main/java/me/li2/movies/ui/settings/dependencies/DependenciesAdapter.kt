/*
 * Created by Weiyi Li on 2020-06-04.
 * https://github.com/li2
 */
package me.li2.movies.ui.settings.dependencies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.databinding.DependencyItemViewBinding
import me.li2.movies.ui.settings.SettingsViewModel

class DependenciesAdapter(private val items: List<DependencyItem>,
                          private val viewModel: SettingsViewModel,
                          private val onLinkClick: (String) -> Unit)
    : RecyclerView.Adapter<DependenciesAdapter.DependencyItemViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DependencyItemViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: DependencyItemViewBinding = DataBindingUtil.inflate(inflater, R.layout.dependency_item_view, parent, false)
        return DependencyItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DependencyItemViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    inner class DependencyItemViewHolder(binding: DependencyItemViewBinding)
        : BaseViewHolder<DependencyItem, DependencyItemViewBinding>(binding) {

        override fun bind(item: DependencyItem, position: Int) {
            binding.root.clicks().throttleFirstShort().subscribe {
                onLinkClick(item.url)
            }

            binding.url = item.url

            viewModel.getLinkPreview(item.url) { linkPreview ->
                binding.linkPreview = linkPreview
                binding.imageUri = linkPreview.imageFile?.toUri().toString()
            }
        }
    }
}

data class DependencyItem(
        val url: String,
        val showThumbnail: Boolean = true)