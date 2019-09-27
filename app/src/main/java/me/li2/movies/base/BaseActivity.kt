package me.li2.movies.base

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

abstract class BaseActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
