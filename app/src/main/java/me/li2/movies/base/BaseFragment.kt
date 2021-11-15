/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.base

import android.os.Bundle
import android.view.View
import dagger.android.support.DaggerFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseFragment : DaggerFragment() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view, savedInstanceState)
        renderUI()
    }

    open fun initUi(view: View, savedInstanceState: Bundle?) {}

    open fun renderUI() {}

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }
}
