/*
 * Created by Weiyi Li on 2020-06-04.
 * https://github.com/li2
 */
package me.li2.movies.ui.settings.dependencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import me.li2.android.common.number.dpToPx
import me.li2.android.view.list.LinearSpacingDecoration
import me.li2.android.view.navigation.setToolbar
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.DependenciesFragmentBinding
import me.li2.movies.ui.settings.SettingsViewModel
import me.li2.movies.util.openUrl

class DependenciesFragment : BaseFragment() {

    private lateinit var binding: DependenciesFragmentBinding
    private val viewModel by navGraphViewModels<SettingsViewModel>(R.id.settingsGraph)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dependencies_fragment, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        activity?.setToolbar(binding.toolbar, "Dependencies")

        binding.dependenciesRecyclerView.apply {
            val items = Dependencies.getAllDependencies().map { DependencyItem(it) }
            this.adapter = DependenciesAdapter(items, viewModel, onLinkClick = { url ->
                requireContext().openUrl(url)
            })
            this.addItemDecoration(LinearSpacingDecoration(RecyclerView.VERTICAL, 4.dpToPx(requireContext())))
        }
    }
}
