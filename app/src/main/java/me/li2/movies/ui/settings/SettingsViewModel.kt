/*
 * Created by Weiyi Li on 2020-06-05.
 * https://github.com/li2
 */
package me.li2.movies.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mega4tech.linkpreview.GetLinkPreviewListener
import com.mega4tech.linkpreview.LinkPreview
import com.mega4tech.linkpreview.LinkUtil
import me.li2.movies.util.ui
import timber.log.Timber.e

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val cachedLinkPreviewMap: MutableMap<String, LinkPreview> = mutableMapOf()

    fun getLinkPreview(url: String, onSuccess: (LinkPreview) -> Unit) {
        cachedLinkPreviewMap[url]?.let {
            onSuccess(it)
            return
        }
        LinkUtil.getInstance().getLinkPreview(getApplication(), url, object : GetLinkPreviewListener {
            override fun onSuccess(link: LinkPreview?) {
                if (link != null) {
                    cachedLinkPreviewMap[url] = link
                    ui {
                        onSuccess(link)
                    }
                }
            }

            override fun onFailed(exception: Exception?) {
                e(exception, "failed to load preview of $url")
            }
        })
    }
}