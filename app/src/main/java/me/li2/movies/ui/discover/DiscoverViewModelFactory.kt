/*
 * Created by Weiyi Li on 2020-05-16.
 * https://github.com/li2
 */
package me.li2.movies.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.li2.movies.data.repository.Repository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class DiscoverViewModelFactory @Inject constructor(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DiscoverViewModel(repository) as T
    }
}
