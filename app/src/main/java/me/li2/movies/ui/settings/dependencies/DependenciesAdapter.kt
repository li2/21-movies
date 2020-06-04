package me.li2.movies.ui.settings.dependencies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.mega4tech.linkpreview.GetLinkPreviewListener
import com.mega4tech.linkpreview.LinkPreview
import com.mega4tech.linkpreview.LinkUtil
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.databinding.DependencyItemViewBinding
import timber.log.Timber.e

class DependenciesAdapter(private val items: List<DependencyItem>,
                          private val onLinkClick: (String) -> Unit)
    : RecyclerView.Adapter<DependenciesAdapter.DependencyItemViewHolder>() {

    private val cachedLinkPreviewMap: MutableMap<String, LinkPreview> = mutableMapOf()

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

            getLinkPreview(item.url) { linkPreview ->
                binding.linkPreview = linkPreview
                binding.imageUri = linkPreview.imageFile?.toUri().toString()
            }
        }

        private fun getLinkPreview(url: String, onSuccess: (LinkPreview) -> Unit) {
            cachedLinkPreviewMap[url]?.let {
                onSuccess(it)
                return
            }

            LinkUtil.getInstance().getLinkPreview(binding.root.context, url, object : GetLinkPreviewListener {
                override fun onSuccess(link: LinkPreview?) {
                    if (link != null) {
                        cachedLinkPreviewMap[url] = link
                        onSuccess(link)
                    }
                }

                override fun onFailed(exception: Exception?) {
                    e(exception, "failed to load preview of $url")
                }
            })
        }
    }
}

data class DependencyItem(
        val url: String,
        val showThumbnail: Boolean = true)