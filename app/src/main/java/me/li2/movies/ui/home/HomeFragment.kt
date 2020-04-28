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
import me.li2.android.common.number.orZero
import me.li2.android.view.popup.toast
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.databinding.HomeFragmentBinding
import me.li2.movies.ui.widgets.moviescarousel.MovieCarouselAdapter
import me.li2.movies.ui.widgets.moviessummary.MovieSummaryHAdapter
import me.li2.movies.util.*
import timber.log.Timber.e
import java.util.concurrent.TimeUnit

class HomeFragment : BaseFragment(), ViewPager2AutoScrollHelper, KeepRootViewAware {
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
        return saveRootViewIfNeeded {
            binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
            binding.root
        }
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(this)

        initializeRootViewIfNeeded {
            binding.executePendingBindings()
            binding.movieCarouselViewPager.ignorePullToRefresh(binding.swipeRefreshLayout)
            binding.movieCarouselPagerIndicator.setViewPager2(binding.movieCarouselViewPager)

            val offsetPx = 48.dpToPx(requireContext())
            val pageMarginPx = 18.dpToPx(requireContext())
            binding.movieCarouselViewPager.showPartialLeftAndRightPages(offsetPx, pageMarginPx, CardPageTransformer(0.85f))
        }

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
        observeOnView(topMoviesLiveData) {
            binding.movieCarouselItems = it.data
            binding.movieCarouselPagerIndicator.count = it.data?.size.orZero()
            bindLoadingStatus(it)
        }

        observeOnView(nowPlayingMoviesLiveData) {
            binding.nowPlayingItems = it.data
            bindLoadingStatus(it)
        }

        observeOnView(upcomingMoviesLiveData) {
            binding.upcomingItems = it.data
            bindLoadingStatus(it)
        }

        observeOnView(popularMoviesLiveData) {
            binding.popularItems = it.data
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
