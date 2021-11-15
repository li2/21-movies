/*
 * Created by Weiyi Li on 2020-04-20.
 * https://github.com/li2
 */
package me.li2.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.plusAssign
import me.li2.android.common.arch.observeOnView
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.HomeFragmentBinding
import me.li2.movies.ui.movies.*
import me.li2.movies.util.*
import javax.inject.Inject

class HomeFragment : BaseFragment(), RootViewStore {
    private lateinit var binding: HomeFragmentBinding

    @Inject
    lateinit var viewModelFactory: HomeViewModelFactory
    private val viewModel by activityViewModels<HomeViewModel> { viewModelFactory }

    override var rootView: View? = null
    override var hasInitializedRootView: Boolean = false
    override var hasInitializedOptionsMenu: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createRootViewIfNeeded {
            binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
            binding.root
        }
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        initializeRootViewIfNeeded {
            doNothing()
        }

        binding.moviesCarouselView.attachToLifecycleOwner(viewLifecycleOwner)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getHomeScreenData(true)
        }

        compositeDisposable += binding.moviesCarouselView.onMovieClicks.subscribe { (_, movieItem) ->
            removeContainerExitTransition()
            navigate(HomeFragmentDirections.showMovieDetail(movieItem))
        }

        compositeDisposable += Observable.mergeArray(
            binding.nowPlayingMovieListView.onMovieClicks,
            binding.upcomingMovieListView.onMovieClicks,
            binding.popularMovieListView.onMovieClicks,
            binding.topRatedMovieListView.onMovieClicks
        ).subscribe { (view, movieItem) ->
            setUpContainerExitTransition(R.id.swipeRefreshLayout)
            val extras = FragmentNavigatorExtras(view to ViewCompat.getTransitionName(view).orEmpty())
            navController().navigate(HomeFragmentDirections.showMovieDetail(movieItem), extras)
        }

        compositeDisposable += Observable.mergeArray(
            binding.nowPlayingMovieListView.onMoreClicks.map { NowPlayingCategory },
            binding.upcomingMovieListView.onMoreClicks.map { UpcomingCategory },
            binding.popularMovieListView.onMoreClicks.map { PopularCategory },
            binding.topRatedMovieListView.onMoreClicks.map { TopRatedCategory }
        ).subscribe {
            removeContainerExitTransition()
            navigateSlideInOut(HomeFragmentDirections.showMoviesList(it))
        }
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(isLoading) {
            binding.isLoading = it
        }

        observeOnView(trendingMovies) {
            binding.movieCarouselItems = it
        }

        observeOnView(nowPlaying) {
            binding.nowPlayingMovies = it
        }

        observeOnView(upcomingMovies) {
            binding.upcomingMovies = it
        }

        observeOnView(popularMovies) {
            binding.popularMovies = it
        }

        observeOnView(topMovies) {
            binding.topRatedMovies = it
        }
    }
}
