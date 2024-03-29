/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

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
