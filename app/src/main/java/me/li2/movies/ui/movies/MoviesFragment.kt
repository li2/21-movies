/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.ui.movies

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.kotlin.plusAssign
import me.li2.android.common.arch.Resource.Status.LOADING
import me.li2.android.common.arch.observeOnView
import me.li2.android.common.rx.throttleFirstShort
import me.li2.android.view.list.DividerItemDecoration
import me.li2.android.view.list.onScrolledBottom
import me.li2.android.view.navigation.setToolbar
import me.li2.android.view.system.hideKeyboard
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.data.model.MapperUI
import me.li2.movies.databinding.MoviesFragmentBinding
import me.li2.movies.ui.filter.FilterBottomSheet
import me.li2.movies.ui.sort.SortBottomSheet
import me.li2.movies.ui.widgets.movies.MovieListAdapter
import me.li2.movies.ui.widgets.movies.MovieListLayoutType.LINEAR_LAYOUT_VERTICAL
import me.li2.movies.ui.widgets.paging.PagingItemAdapter
import me.li2.movies.util.*

class MoviesFragment : BaseFragment(), RootViewStore {

    override var rootView: View? = null
    override var hasInitializedRootView: Boolean = false
    override var hasInitializedOptionsMenu: Boolean = false

    private lateinit var binding: MoviesFragmentBinding
    private val args by navArgs<MoviesFragmentArgs>()
    private val viewModel by viewModels<MoviesViewModel>()

    // trending and watchlist movies list is not paging supported
    private val isPagingSupported: Boolean
        get() = args.moviesCategory !is TrendingCategory
                && args.moviesCategory !is Watchlist

    private val moviesAdapter = MovieListAdapter(LINEAR_LAYOUT_VERTICAL)
    private val pagingAdapter = PagingItemAdapter()

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setUpContainerExitTransition(R.id.root)
        viewModel.getMovies(args.moviesCategory)
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onResume() {
        super.onResume()
        if (args.moviesCategory is Watchlist) {
            // watchlist can be changed when user remove movie
            viewModel.getMovies(Watchlist, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return createRootViewIfNeeded {
            binding = DataBindingUtil.inflate(inflater, R.layout.movies_fragment, container, false)
            binding.root
        }
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        initializeRootViewIfNeeded {
            fixContainerExitTransition()
            binding.executePendingBindings()

            binding.moviesRecyclerView.apply {
                adapter = if (isPagingSupported) {
                    MergeAdapter(moviesAdapter, pagingAdapter)
                } else {
                    moviesAdapter
                }
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            }
        }

        activity?.setToolbar(binding.toolbar, title = args.moviesCategory.label)

        compositeDisposable += moviesAdapter.onMovieClicks.throttleFirstShort().subscribe { (view, movieItem) ->
            val extras = FragmentNavigatorExtras(view to ViewCompat.getTransitionName(view).orEmpty())
            navController().navigate(MoviesFragmentDirections.showMovieDetail(movieItem), extras)
        }

        compositeDisposable += pagingAdapter.retryClicks.throttleFirstShort().subscribe {
            viewModel.getMovies(args.moviesCategory)
        }

        compositeDisposable += binding.moviesRecyclerView.onScrolledBottom()
                .throttleFirstShort() // avoid duplicate API calls, 21note
                .subscribe {
                    // don't load next page if it's in requesting, or error, or already on the last page. 21note
                    if (viewModel.canLoadMoreMovies && isPagingSupported) {
                        if (args.moviesCategory is QueryCategory
                                && this::searchView.isInitialized
                                && this.searchView.query.isNotEmpty()) {
                            viewModel.getMovies(QueryCategory(searchView.query.toString()))
                        } else {
                            viewModel.getMovies(args.moviesCategory)
                        }
                    }
                }

        binding.moviesEmptyView.discoverButton.clicks().throttleFirstShort().subscribe {
            navController().navigate(MoviesFragmentDirections.discover())
        }
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(movies) {
            moviesAdapter.submitList(it.data?.results)
            if (isFirstPage) {
                binding.moviesRecyclerView.scrollToPosition(0)
            }
            if (isPagingSupported) {
                pagingAdapter.pagingState = MapperUI.toPagingState(it)
            }
            binding.shimmerContainer.shimmer.showAnimation(it.status == LOADING && it.data?.page == null)
            binding.moviesEmptyView.isVisible = it.status != LOADING && it.data?.results.isNullOrEmpty()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movies_menu, menu)
        initializeOptionsMenuIfNeeded {
            setUpSearchView(menu.findItem(R.id.search))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> showFilterBottomSheet()
            R.id.sort -> showSortBottomSheet()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpSearchView(searchMenuItem: MenuItem) {
        // only show search menu item for Search type
        if (args.moviesCategory !is QueryCategory) {
            searchMenuItem.isVisible = false
            return
        }
        searchMenuItem.isVisible = true
        val initialQuery = (args.moviesCategory as QueryCategory).query
        searchView = searchMenuItem.actionView as SearchView
        searchView.init(searchMenuItem, initialQuery, "Search Movies") { queryText ->
            if (!queryText.isNullOrEmpty()) {
                hideKeyboard()
                viewModel.getMovies(QueryCategory(queryText), true)
            }
        }
    }

    private fun showFilterBottomSheet(): Boolean {
        FilterBottomSheet(requireContext(),
                initialFilter = viewModel.filter,
                onApplyClick = {
                    viewModel.filterMovies { this.copy(it) }
                }
        ).show()
        return true
    }

    private fun showSortBottomSheet(): Boolean {
        SortBottomSheet(requireContext(),
                sortItems = viewModel.sort.buildSortItems(),
                onSortChange = { sortType, descending ->
                    viewModel.sortMovies(sortType, descending)
                }
        ).show()
        return true
    }
}
