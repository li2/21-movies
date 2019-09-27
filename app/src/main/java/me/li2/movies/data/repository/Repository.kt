package me.li2.movies.data.repository

import io.reactivex.disposables.CompositeDisposable
import me.li2.movies.App
import me.li2.movies.data.local.DBDataSource
import me.li2.movies.data.remote.RemoteDataSource
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class Repository : KodeinAware {
    override val kodein by kodein(App.context)
    private val remoteDataSource by instance<RemoteDataSource>()
    private val dbDataSource by instance<DBDataSource>()

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

}
