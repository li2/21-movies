package me.li2.movies.util

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

private const val CLICK_THROTTLE_DELAY = 500L

fun <T> Observable<T>.throttleFirstShort() = this.throttleFirst(CLICK_THROTTLE_DELAY, TimeUnit.MILLISECONDS)!!
