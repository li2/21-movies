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
import androidx.fragment.app.viewModels
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.plusAssign
import me.li2.android.common.arch.observeOnView
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.DiscoverFragmentBinding
import me.li2.movies.ui.movies.QueryCategory
import me.li2.movies.util.endIconClicks
import me.li2.movies.util.imeActionSearchClicks
import me.li2.movies.util.navigateSlideInOut

class DiscoverFragment : BaseFragment() {

    private lateinit var binding: DiscoverFragmentBinding
    private val viewModel by viewModels<DiscoverViewModel>()

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
                    navigateSlideInOut(DiscoverFragmentDirections.showMoviesList(QueryCategory(query)))
                }
            }
        }

        Observable.merge(
                binding.genresCategoriesGroupView.onCategoryClicks,
                binding.mainCategoriesGroupView.onCategoryClicks
        ).subscribe {
            navigateSlideInOut(DiscoverFragmentDirections.showMoviesList(it))
        }
    }

    override fun renderUI() = with(viewModel) {
        binding.mainCategories = mainCategories

        observeOnView(genresCategories) {
            binding.genresCategories = it
        }
    }
}