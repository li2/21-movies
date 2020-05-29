/*
 * Created by Weiyi Li on 2020-05-18.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.moviescarousel

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.rd.PageIndicatorView
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status
import me.li2.android.common.arch.Resource.Status.LOADING
import me.li2.android.common.number.dpToPx
import me.li2.android.common.number.orZero
import me.li2.android.view.list.CardPageTransformer
import me.li2.android.view.list.ViewPager2AutoScrollHelper
import me.li2.android.view.list.showPartialLeftAndRightPages
import me.li2.movies.R
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.data.model.noData
import me.li2.movies.util.SampleProvider
import me.li2.movies.util.setViewPager2
import me.li2.movies.util.showAnimation
import java.util.concurrent.TimeUnit

class MoviesCarouselView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr),
        ViewPager2AutoScrollHelper {

    private val viewPager: ViewPager2
    private val pagerIndicator: PageIndicatorView
    private val stateLayout: View
    private val stateTextView: TextView
    private val shimmerLayout: ShimmerFrameLayout

    override val autoScrollViewPager get() = viewPager
    override val viewPagerAutoScrollPeriod = Pair(5L, TimeUnit.SECONDS)
    override val shouldViewPagerAutoScroll = BehaviorSubject.createDefault(true)!!
    override var viewPagerAutoScrollTask: Disposable? = null

    private val moviesAdapter: MovieCarouselAdapter = MovieCarouselAdapter()
    val onMovieClicks = moviesAdapter.onMovieClicks

    var movies: Resource<List<MovieItemUI>>? = Resource.loading(emptyList())
        set(value) {
            if (value == null) return
            field = value
            // update list
            moviesAdapter.submitList(value.data) {
                if (viewPager.currentItem == 0) {
                    viewPager.setCurrentItem(moviesAdapter.getCarouselInitialDisplayPosition(), false)
                }
            }
            pagerIndicator.count = value.data?.size.orZero()
            // update loading state
            shimmerLayout.showAnimation(value.status == LOADING && value.noData())
            // update empty or error message
            val stateMessage = when {
                value.status == Status.SUCCESS && value.noData() -> "No trending movies"
                value.status == Status.ERROR && value.noData() -> "\uD83D\uDE28 Wooops ${value.exception?.message}"
                else -> null
            }
            stateTextView.text = stateMessage
            stateLayout.isVisible = stateMessage != null
        }

    init {
        val root = inflate(context, R.layout.movies_carousel_view, this)
        viewPager = root.findViewById(R.id.movieCarouselViewPager)
        pagerIndicator = root.findViewById(R.id.movieCarouselPagerIndicator)
        stateTextView = root.findViewById(R.id.stateTextView)
        stateLayout = root.findViewById(R.id.stateLayout)
        shimmerLayout = root.findViewById(R.id.shimmerLayout)

        viewPager.adapter = moviesAdapter
        viewPager.showPartialLeftAndRightPages(
                offset = 64.dpToPx(context),
                pageMargin = 18.dpToPx(context),
                transformer = CardPageTransformer(0.85f))
        pagerIndicator.setViewPager2(viewPager)

        if (isInEditMode) {
            // show sample data when shown in the IDE preview
            movies = Resource.success(SampleProvider.movieItemList())
        }
    }

    fun attachToLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        addAutoScrollTaskObserver(lifecycleOwner)
    }
}