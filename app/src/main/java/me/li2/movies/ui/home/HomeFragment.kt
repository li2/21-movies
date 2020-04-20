package me.li2.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import me.li2.android.common.arch.Resource.Status.*
import me.li2.android.common.arch.observeOnView
import me.li2.android.common.number.orZero
import me.li2.android.view.popup.toast
import me.li2.android.view.system.hideStatusBar
import me.li2.android.view.system.showStatusBar
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.HomeFragmentBinding
import me.li2.movies.ui.home.top.TopItemUI
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
        activity?.showStatusBar()
        stopViewPagerAutoScrollTask()
        super.onDestroyView()
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        activity?.hideStatusBar()
        binding.executePendingBindings()
        binding.topItemsViewPager.ignorePullToRefresh(binding.swipeRefreshLayout)
        binding.topItemsPagerIndicator.setViewPager2(binding.topItemsViewPager)

        val offsetPx = 48.dpToPx(requireContext())
        val pageMarginPx = 18.dpToPx(requireContext())
        binding.topItemsViewPager.showPartialLeftAndRightPages(offsetPx, pageMarginPx, CardPageTransformer(0.85f))

        startViewPagerAutoScrollTask()
        disableViewPagerAutoScrollOnTouch()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getTopItems(true)
        }
    }

    override fun initViewModel() = with(viewModel) {
        getTopItems()
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(topItemsLiveData) {
            when (it.status) {
                LOADING -> bindTopItems(it.data, true)
                SUCCESS -> bindTopItems(it.data, false)
                ERROR -> {
                    binding.isLoading = false
                    toast(it.exception.toString())
                    e(it.exception, "failed to get movies")
                }
            }
        }
    }

    private fun bindTopItems(topItems: List<TopItemUI>?, isLoading: Boolean) {
        binding.topItems = topItems
        binding.isLoading = isLoading
        binding.topItemsPagerIndicator.count = topItems?.size.orZero()
    }
}
