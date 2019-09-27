package me.li2.movies.data.local

import me.li2.movies.App
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class DBDataSource: KodeinAware {
    override val kodein by kodein(App.context)
}
