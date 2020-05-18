package me.li2.movies.ui.widgets.moviescarousel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
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
import me.li2.movies.databinding.MoviesCarouselViewBinding
import me.li2.movies.util.setViewPager2
import java.util.concurrent.TimeUnit

class MoviesCarouselView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr),
        ViewPager2AutoScrollHelper {

    private val binding: MoviesCarouselViewBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.movies_carousel_view, this, true)

    override val autoScrollViewPager get() = binding.movieCarouselViewPager
    override val viewPagerAutoScrollPeriod = Pair(5L, TimeUnit.SECONDS)
    override val shouldViewPagerAutoScroll = BehaviorSubject.createDefault(true)
    override var viewPagerAutoScrollTask: Disposable? = null

    val onMovieClicks
        get() = ((binding.movieCarouselViewPager.adapter) as MovieCarouselAdapter).onMovieClicks

    var movies: Resource<List<MovieItemUI>>? = Resource.loading(emptyList())
        set(value) {
            if (value == null) return
            field = value
            binding.movieItems = value.data
            binding.isLoading = value.status == LOADING && value.noData()
            binding.emptyOrErrorMessage = when {
                value.status == Status.SUCCESS && value.noData() -> "No trending movies"
                value.status == Status.ERROR && value.noData() -> "\uD83D\uDE28 Wooops ${value.exception?.message}"
                else -> null
            }
            binding.movieCarouselPagerIndicator.count = value.data?.size.orZero()
        }

    init {
        binding.executePendingBindings()
        binding.movieCarouselViewPager.showPartialLeftAndRightPages(
                offset = 64.dpToPx(context),
                pageMargin = 18.dpToPx(context),
                transformer = CardPageTransformer(0.85f))
        binding.movieCarouselPagerIndicator.setViewPager2(binding.movieCarouselViewPager)
    }

    fun attachToLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        addAutoScrollTaskObserver(lifecycleOwner)
    }
}