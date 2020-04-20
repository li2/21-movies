package me.li2.movies.util

import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import me.li2.android.common.number.orZero
import java.util.concurrent.TimeUnit

interface ViewPager2AutoScrollHelper {
    val autoScrollViewPager: ViewPager2
    val viewPagerAutoScrollPeriod: Pair<Long, TimeUnit>
    val shouldViewPagerAutoScroll: BehaviorSubject<Boolean>
    var viewPagerAutoScrollTask: Disposable?

    private fun createAutoScrollTask(): Disposable {
        return shouldViewPagerAutoScroll
                .switchMap {
                    if (it) {
                        return@switchMap Observable.interval(viewPagerAutoScrollPeriod.first, viewPagerAutoScrollPeriod.first, viewPagerAutoScrollPeriod.second)
                    } else {
                        return@switchMap Observable.empty<Long>()
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val itemCount = autoScrollViewPager.adapter?.itemCount.orZero()
                    if (itemCount > 0) {
                        val currentItem = autoScrollViewPager.currentItem
                        val isLastItem = currentItem == itemCount - 1
                        autoScrollViewPager.currentItem = if (!isLastItem) currentItem + 1 else 0
                    }
                }
    }

    fun startViewPagerAutoScrollTask() {
        viewPagerAutoScrollTask = createAutoScrollTask()
    }

    fun stopViewPagerAutoScrollTask() {
        viewPagerAutoScrollTask?.let { task ->
            if (!task.isDisposed) {
                task.dispose()
            }
        }
    }

    fun disableViewPagerAutoScrollOnTouch() {
        autoScrollViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                shouldViewPagerAutoScroll.onNext(state != SCROLL_STATE_DRAGGING)
            }
        })
    }
}