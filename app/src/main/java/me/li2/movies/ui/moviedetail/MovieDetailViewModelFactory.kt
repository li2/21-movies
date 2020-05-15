package me.li2.movies.ui.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.li2.movies.data.model.MovieItemUI

@Suppress("UNCHECKED_CAST")
class MovieDetailViewModelFactory(private val movieItem: MovieItemUI) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailViewModel(movieItem) as T
    }
}