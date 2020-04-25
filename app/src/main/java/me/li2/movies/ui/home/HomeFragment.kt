package me.li2.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
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
import me.li2.movies.ui.home.centre.CentreItemsAdapter
import me.li2.movies.ui.home.top.TopItemsAdapter
import me.li2.movies.util.*
import timber.log.Timber.e
import java.util.concurrent.TimeUnit

class HomeFragment : BaseFragment(), ViewPager2AutoScrollHelper {
    private lateinit var binding: HomeFragmentBinding
    private val viewModel by activityViewModels<HomeViewModel>()

    override val autoScrollViewPager get() = binding.topItemsViewPager
    override val viewPagerAutoScrollPeriod = Pair(5L, TimeUnit.SECONDS)
    override val shouldViewPagerAutoScroll = BehaviorSubject.createDefault(true)
    override var viewPagerAutoScrollTask: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        stopViewPagerAutoScrollTask()
        super.onDestroyView()
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        binding.executePendingBindings()
        binding.topItemsViewPager.ignorePullToRefresh(binding.swipeRefreshLayout)
        binding.topItemsPagerIndicator.setViewPager2(binding.topItemsViewPager)

        val offsetPx = 48.dpToPx(requireContext())
        val pageMarginPx = 18.dpToPx(requireContext())
        binding.topItemsViewPager.showPartialLeftAndRightPages(offsetPx, pageMarginPx, CardPageTransformer(0.85f))

        startViewPagerAutoScrollTask()
        disableViewPagerAutoScrollOnTouch()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getHomeData(true)
        }

        compositeDisposable += Observable.merge(
                (binding.topItemsViewPager.adapter as TopItemsAdapter).itemClicks,
                (binding.nowPlayingMoviesRecyclerView.adapter as CentreItemsAdapter).itemClicks,
                (binding.upcomingMoviesRecyclerView.adapter as CentreItemsAdapter).itemClicks,
                (binding.popularMoviesRecyclerView.adapter as CentreItemsAdapter).itemClicks
        ).subscribe { (posterImageView, movieItem) ->
            val extras = FragmentNavigatorExtras(posterImageView to getString(R.string.transition_name_movie) + movieItem.id)
            navController().navigate(HomeFragmentDirections.showMovieDetail(movieItem), extras)
        }
    }

    override fun initViewModel() = with(viewModel) {
        getHomeData()
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(topMoviesLiveData) {
            binding.topItems = it.data
            binding.topItemsPagerIndicator.count = it.data?.size.orZero()
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

    private fun bindLoadingStatus(resource: Resource<List<MovieItemUI>>) {
        binding.isLoading = resource.status == LOADING
        if (resource.status == ERROR) {
            toast(resource.exception.toString())
            e(resource.exception, "failed to get movies")
        }
    }
}
