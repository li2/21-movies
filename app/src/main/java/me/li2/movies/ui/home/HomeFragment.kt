package me.li2.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.ERROR
import me.li2.android.common.arch.Resource.Status.LOADING
import me.li2.android.common.arch.observeOnView
import me.li2.android.common.number.dpToPx
import me.li2.android.common.number.orZero
import me.li2.android.view.list.CardPageTransformer
import me.li2.android.view.list.ViewPager2AutoScrollHelper
import me.li2.android.view.list.ignorePullToRefresh
import me.li2.android.view.list.showPartialLeftAndRightPages
import me.li2.android.view.popup.toast
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.databinding.HomeFragmentBinding
import me.li2.movies.ui.widgets.moviescarousel.MovieCarouselAdapter
import me.li2.movies.ui.widgets.moviessummary.MovieSummaryHAdapter
import me.li2.movies.util.RootViewStore
import me.li2.movies.util.navigate
import me.li2.movies.util.setViewPager2
import timber.log.Timber.e
import java.util.concurrent.TimeUnit

class HomeFragment : BaseFragment(), ViewPager2AutoScrollHelper, RootViewStore {
    private lateinit var binding: HomeFragmentBinding
    private val viewModel by activityViewModels<HomeViewModel>()

    override val autoScrollViewPager get() = binding.movieCarouselViewPager
    override val viewPagerAutoScrollPeriod = Pair(5L, TimeUnit.SECONDS)
    override val shouldViewPagerAutoScroll = BehaviorSubject.createDefault(true)
    override var viewPagerAutoScrollTask: Disposable? = null

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
            binding.executePendingBindings()
            binding.movieCarouselViewPager.ignorePullToRefresh(binding.swipeRefreshLayout)
            binding.movieCarouselViewPager.showPartialLeftAndRightPages(
                    offset = 64.dpToPx(requireContext()),
                    pageMargin = 18.dpToPx(requireContext()),
                    transformer = CardPageTransformer(0.85f))
            binding.movieCarouselPagerIndicator.setViewPager2(binding.movieCarouselViewPager)
        }

        addAutoScrollTaskObserver(viewLifecycleOwner)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getHomeScreenData(true)
        }

        compositeDisposable += Observable.merge(
                (binding.movieCarouselViewPager.adapter as MovieCarouselAdapter).itemClicks,
                (binding.nowPlayingMoviesRecyclerView.adapter as MovieSummaryHAdapter).itemClicks,
                (binding.upcomingMoviesRecyclerView.adapter as MovieSummaryHAdapter).itemClicks,
                (binding.popularMoviesRecyclerView.adapter as MovieSummaryHAdapter).itemClicks
        ).subscribe { (_, movieItem) ->
            navigate(HomeFragmentDirections.showMovieDetail(movieItem))
        }
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(topMovies) {
            binding.movieCarouselItems = it.data
            binding.movieCarouselPagerIndicator.count = it.data?.size.orZero()
            binding.isLoadingTopMovies = it.status == LOADING && it.data.isNullOrEmpty()
            bindLoadingStatus(it)
        }

        observeOnView(nowPlaying) {
            binding.nowPlayingItems = it.data
            binding.isLoadingNowPlayingMovies = it.status == LOADING && it.data.isNullOrEmpty()
            bindLoadingStatus(it)
        }

        observeOnView(upcomingMovies) {
            binding.upcomingItems = it.data
            binding.isLoadingUpcomingMovies = it.status == LOADING && it.data.isNullOrEmpty()
            bindLoadingStatus(it)
        }

        observeOnView(popularMovies) {
            binding.popularItems = it.data
            binding.isLoadingPopularMovies = it.status == LOADING && it.data.isNullOrEmpty()
            bindLoadingStatus(it)
        }
    }

    private fun bindLoadingStatus(resource: Resource<List<MovieItemUI>?>) {
        binding.isLoading = resource.status == LOADING
        if (resource.status == ERROR) {
            toast(resource.exception.toString())
            e(resource.exception, "failed to get movies")
        }
    }
}
