/*
 * Created by Weiyi Li on 2020-05-31.
 * https://github.com/li2
 */
package me.li2.movies.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.widget.editorActionEvents
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.plusAssign
import me.li2.android.common.arch.observeOnView
import me.li2.android.view.text.endIconClicks
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.DiscoverFragmentBinding
import me.li2.movies.ui.movies.QueryCategory
import me.li2.movies.util.navigateSlideInOut
import javax.inject.Inject

class DiscoverFragment : BaseFragment() {

    private lateinit var binding: DiscoverFragmentBinding

    @Inject
    lateinit var viewModelFactory: DiscoverViewModelFactory
    private val viewModel by viewModels<DiscoverViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.discover_fragment, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {

        compositeDisposable += Observable.merge(
            binding.searchInputLayout.endIconClicks(),
            binding.searchEditText.editorActionEvents().flatMap {
                if (it.actionId == IME_ACTION_SEARCH) Observable.just(Unit) else Observable.empty()
            }
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