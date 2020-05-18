/*
 * Created by Weiyi Li on 2020-04-20.
 * https://github.com/li2
 */
package me.li2.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import me.li2.android.common.arch.observeOnView
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.HomeFragmentBinding
import me.li2.movies.util.RootViewStore
import me.li2.movies.util.doNothing
import me.li2.movies.util.navigate

class HomeFragment : BaseFragment(), RootViewStore {
    private lateinit var binding: HomeFragmentBinding
    private val viewModel by activityViewModels<HomeViewModel>()

    override var rootView: View? = null
    override var hasInitializedRootView: Boolean = false

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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

        compositeDisposable += Observable.mergeArray(
                binding.moviesCarouselView.onMovieClicks,
                binding.nowPlayingMovieListView.onMovieClicks,
                binding.upcomingMovieListView.onMovieClicks,
                binding.popularMovieListView.onMovieClicks,
                binding.topRatedMovieListView.onMovieClicks
        ).subscribe { (_, movieItem) ->
            navigate(HomeFragmentDirections.showMovieDetail(movieItem))
        }
    }

    override fun renderUI() = with(viewModel) {
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
