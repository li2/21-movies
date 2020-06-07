/*
 * Created by Weiyi Li on 2020-05-31.
 * https://github.com/li2
 */
package me.li2.movies.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import com.jakewharton.rxbinding4.view.clicks
import me.li2.android.common.framework.openAppSettings
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.SettingsFragmentBinding
import me.li2.movies.ui.movies.Watchlist
import me.li2.movies.util.*

class SettingsFragment : BaseFragment() {

    private lateinit var binding: SettingsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        val context = view.context

        binding.watchlistItemView.clicks().throttleFirstShort().subscribe {
            navigateSlideInOut(SettingsFragmentDirections.showMoviesList(Watchlist))
        }

        binding.themeSettingItemView.clicks().throttleFirstShort().subscribe {
            showThemeMenu(binding.themeSettingItemView)
        }

        binding.appSettingItemView.clicks().throttleFirstShort().subscribe {
            context.openAppSettings(context.packageName)
        }

        binding.codeItemView.clicks().throttleFirstShort().subscribe {
            context.openUrl(Constants.SOURCE_CODE_URL)
        }

        binding.dependenciesItemView.clicks().throttleFirstShort().subscribe {
            navigateSlideInOut(SettingsFragmentDirections.showDependencies())
        }

        binding.playstoreItemView.clicks().throttleFirstShort().subscribe {
            context.openUrl(Constants.PLAYSTORE_URL)
        }

        binding.settingsFooterView.versionName = context.getVersionName()
    }

    private fun showThemeMenu(anchor: View) {
        val popup = PopupMenu(anchor.context, anchor)
        popup.menuInflater.inflate(R.menu.theme_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            val mode = when (menuItem.itemId) {
                R.id.themeLight -> ThemeHelper.LIGHT_MODE
                R.id.themeDark -> ThemeHelper.DARK_MODE
                else -> ThemeHelper.DEFAULT_MODE
            }
            binding.themeSettingItemView.setTitle(mode)
            ThemeHelper.applyTheme(mode)
            true
        }
        popup.show()
    }
}