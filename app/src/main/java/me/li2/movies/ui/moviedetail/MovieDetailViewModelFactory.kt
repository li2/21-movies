/*
 * Created by Weiyi Li on 2020-05-16.
 * https://github.com/li2
 */
package me.li2.movies.ui.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.data.repository.Repository

@Suppress("UNCHECKED_CAST")
class MovieDetailViewModelFactory @AssistedInject constructor(
    private val repository: Repository,
    @Assisted private val movieItem: MovieItemUI
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailViewModel(repository, movieItem) as T
    }
}

// assisted injection to provide parameter
// https://dagger.dev/dev-guide/assisted-injection.html
@AssistedFactory
interface MovieDetailViewModelAssistedFactory {
    fun create(movieItem: MovieItemUI): MovieDetailViewModelFactory
}
