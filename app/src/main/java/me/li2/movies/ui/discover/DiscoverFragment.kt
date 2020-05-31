/*
 * Created by Weiyi Li on 2020-05-31.
 * https://github.com/li2
 */
package me.li2.movies.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.DiscoverFragmentBinding

class DiscoverFragment: BaseFragment() {

    private lateinit var binding: DiscoverFragmentBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.discover_fragment, container, false)
        return binding.root
    }
}