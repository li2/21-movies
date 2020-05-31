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
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.plusAssign
import me.li2.android.view.system.hideKeyboard
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.DiscoverFragmentBinding
import me.li2.movies.ui.movies.SearchMovieList
import me.li2.movies.util.endIconClicks
import me.li2.movies.util.imeActionSearchClicks
import me.li2.movies.util.navigateSlideInOut

class DiscoverFragment : BaseFragment() {

    private lateinit var binding: DiscoverFragmentBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.discover_fragment, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {

        compositeDisposable += Observable.merge(
                binding.searchInputLayout.endIconClicks(),
                binding.searchEditText.imeActionSearchClicks()
        ).subscribe {
            binding.searchEditText.text.toString().let { query ->
                if (query.isNotEmpty()) {
                    hideKeyboard()
                    navigateSlideInOut(DiscoverFragmentDirections.showMoviesList(SearchMovieList(query)))
                }
            }
        }
    }
}