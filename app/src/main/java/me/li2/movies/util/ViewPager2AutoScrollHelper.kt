/*
 * Created by weiyi on 2020-04-18.
 * https://github.com/li2
 */
package me.li2.movies.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager2.widget.ViewPager2
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import me.li2.android.common.number.orZero
import java.util.concurrent.TimeUnit

/**
 * A lifecycle awareness interface serve as a plugin to provide the ViewPager auto scroll ability.
 *
 * - Added LifecycleObserver to make this interface to be self-sufficient,
 *      need to call viewLifecycleOwner.lifecycle.addObserver(this) onViewCreated.
 * - Having the individual components store their own logic to react to changes in lifecycle status
 *      makes the fragment logic easier to manage.
 */
interface ViewPager2AutoScrollHelper: LifecycleObserver {
    /** the ViewPager instance */
    val autoScrollViewPager: ViewPager2

    /** Auto-scroll period, e.g. Pair(5L, TimeUnit.SECONDS) */
    val viewPagerAutoScrollPeriod: Pair<Long, TimeUnit>

    /** Initialize as BehaviorSubject.createDefault(true) */
    val shouldViewPagerAutoScroll: BehaviorSubject<Boolean>

    /** Initialize as null */
    var viewPagerAutoScrollTask: Disposable?

    private fun createAutoScrollTask(): Disposable {
        autoScrollViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                shouldViewPagerAutoScroll.onNext(state != ViewPager2.SCROLL_STATE_DRAGGING)
            }
        })
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

    /**
     * call when ViewPager data is ready to start auto scroll task.
     * Note that the auto scroll task will be paused when the user starts dragging pages.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startOrResumeViewPagerAutoScrollTask() {
        if (viewPagerAutoScrollTask == null) {
            viewPagerAutoScrollTask = createAutoScrollTask()
        }
        shouldViewPagerAutoScroll.onNext(true)
    }

    /**
     * call to pause auto scroll task.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseViewPagerAutoScrollTask() {
        shouldViewPagerAutoScroll.onNext(false)
    }

    /**
     * call when ViewPager was destroyed. normally on Fragment.onDestroyView()
     * ON_DESTROY is handled on both
     * [androidx.fragment.app.Fragment.performDestroyView] and [androidx.fragment.app.Fragment.performDestroy]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stopViewPagerAutoScrollTask() {
        if (viewPagerAutoScrollTask != null) {
            viewPagerAutoScrollTask?.let { task ->
                if (!task.isDisposed) {
                    task.dispose()
                }
            }
            viewPagerAutoScrollTask = null
        }
    }
}