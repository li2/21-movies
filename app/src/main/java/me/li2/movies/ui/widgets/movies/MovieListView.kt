package me.li2.movies.ui.widgets.movies

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.*
import me.li2.movies.R
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.data.model.noData
import me.li2.movies.databinding.MovieListViewBinding
import me.li2.movies.ui.widgets.movies.MovieListLayoutType.LINEAR_LAYOUT_HORIZONTAL

class MovieItemListView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: MovieListViewBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.movie_list_view, this, true)

    private val moviesAdapter = MovieListAdapter(LINEAR_LAYOUT_HORIZONTAL)

    val onMovieClicks = moviesAdapter.onMovieClicks

    var label: String? = null
        set(value) {
            field = value
            binding.label = value
        }

    var movies: Resource<List<MovieItemUI>>? = Resource.loading(emptyList())
        set(value) {
            if (value == null) return
            field = value
            moviesAdapter.submitList(value.data)
            binding.isLoading = value.status == LOADING && value.noData()
            binding.emptyOrErrorMessage = when {
                value.status == SUCCESS && value.noData() -> "Movies are empty"
                value.status == ERROR && value.noData() -> "\uD83D\uDE28 Wooops ${value.exception?.message}"
                else -> null
            }
        }

    var layoutType: MovieListLayoutType = LINEAR_LAYOUT_HORIZONTAL
        set(value) {
            field = value
            moviesAdapter.layoutType = value
        }

    init {
        binding.moviesRecyclerView.apply {
            adapter = moviesAdapter
            addItemDecoration(layoutType.getItemDecoration(context))
            layoutManager = layoutType.getLayoutManger(context)
        }
    }
}