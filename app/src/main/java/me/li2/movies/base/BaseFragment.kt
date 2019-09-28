package me.li2.movies.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    protected val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view, savedInstanceState)
        initViewModel()
        renderUI()
    }

    open fun initUi(view: View, savedInstanceState: Bundle?) {}

    open fun initViewModel() {}

    open fun renderUI() {}

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }
}
