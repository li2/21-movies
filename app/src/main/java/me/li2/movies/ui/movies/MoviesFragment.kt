/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxkotlin.plusAssign
import me.li2.android.common.arch.Resource.Status.LOADING
import me.li2.android.common.arch.observeOnView
import me.li2.android.common.rx.throttleFirstShort
import me.li2.android.view.list.DividerItemDecoration
import me.li2.android.view.list.onScrolledBottom
import me.li2.android.view.navigation.setToolbar
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.data.model.MapperUI
import me.li2.movies.databinding.MoviesFragmentBinding
import me.li2.movies.ui.widgets.movies.MovieListAdapter
import me.li2.movies.ui.widgets.movies.MovieListLayoutType.LINEAR_LAYOUT_VERTICAL
import me.li2.movies.ui.widgets.paging.PagingItemAdapter
import me.li2.movies.util.fixContainerExitTransition
import me.li2.movies.util.navController
import me.li2.movies.util.setUpContainerExitTransition
import me.li2.movies.util.showAnimation

class MoviesFragment : BaseFragment() {

    private lateinit var binding: MoviesFragmentBinding
    private val args by navArgs<MoviesFragmentArgs>()
    private val viewModel by viewModels<MoviesViewModel>()

    private val moviesAdapter = MovieListAdapter(LINEAR_LAYOUT_VERTICAL)
    private val pagingAdapter = PagingItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpContainerExitTransition(R.id.root)
        viewModel.searchMovies(args.genre)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.movies_fragment, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        fixContainerExitTransition()
        activity?.setToolbar(binding.toolbar, title = args.genre)
        binding.executePendingBindings()

        binding.moviesRecyclerView.apply {
            adapter = MergeAdapter(moviesAdapter, pagingAdapter)
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

        compositeDisposable += moviesAdapter.onMovieClicks.throttleFirstShort().subscribe { (view, movieItem) ->
            val extras = FragmentNavigatorExtras(view to ViewCompat.getTransitionName(view).orEmpty())
            navController().navigate(MoviesFragmentDirections.showMovieDetail(movieItem), extras)
        }

        compositeDisposable += pagingAdapter.retryClicks.throttleFirstShort().subscribe {
            viewModel.searchMovies(args.genre)
        }

        compositeDisposable += binding.moviesRecyclerView.onScrolledBottom()
                .throttleFirstShort() // avoid duplicate API calls, 21note
                .subscribe {
                    // don't load next page if it's in requesting, or error, or already on the last page. 21note
                    if (viewModel.canLoadMoreMovies) {
                        viewModel.searchMovies(args.genre)
                    }
                }
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(movies) {
            moviesAdapter.submitList(it.data?.results)
            pagingAdapter.pagingState = MapperUI.toPagingState(it)
            binding.shimmerContainer.shimmer.showAnimation(it.status == LOADING && it.data?.page == null)
        }
    }
}
