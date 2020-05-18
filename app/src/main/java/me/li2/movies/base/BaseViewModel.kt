/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.base

import androidx.lifecycle.ViewModel
import me.li2.movies.App
import me.li2.movies.data.repository.Repository
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

open class BaseViewModel :
        ViewModel(),
        KodeinAware {

    final override val kodein by kodein(App.context)

    val repository by instance<Repository>()
}
