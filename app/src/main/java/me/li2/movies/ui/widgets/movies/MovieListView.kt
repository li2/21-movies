/*
 * Created by Weiyi Li on 2020-05-18.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.movies

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.*
import me.li2.movies.R
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.data.model.noData
import me.li2.movies.ui.widgets.movies.MovieListLayoutType.LINEAR_LAYOUT_HORIZONTAL
import me.li2.movies.util.SampleProvider
import me.li2.movies.util.showAnimation

class MovieItemListView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val labelTextView: TextView
    private val stateTextView: TextView
    private val recyclerView: RecyclerView
    private val shimmerLayout: ShimmerFrameLayout

    private val moviesAdapter = MovieListAdapter(LINEAR_LAYOUT_HORIZONTAL)

    val onMovieClicks = moviesAdapter.onMovieClicks

    var label: String? = null
        set(value) {
            field = value
            labelTextView.text = value
            labelTextView.isVisible = value != null
        }

    var movies: Resource<List<MovieItemUI>>? = null
        set(value) {
            if (value == null) return
            field = value
            // update list
            moviesAdapter.submitList(value.data)
            // update loading state
            shimmerLayout.showAnimation(value.status == LOADING && value.noData())
            // update empty or error message
            val stateMessage = when {
                value.status == SUCCESS && value.noData() -> "Movies are empty"
                value.status == ERROR && value.noData() -> "\uD83D\uDE28 Wooops ${value.exception?.message}"
                else -> null
            }
            stateTextView.text = stateMessage
            stateTextView.isVisible = stateMessage != null
        }

    var layoutType: MovieListLayoutType = LINEAR_LAYOUT_HORIZONTAL
        set(value) {
            field = value
            moviesAdapter.layoutType = value
        }

    init {
        val root = inflate(context, R.layout.movie_list_view, this)
        labelTextView = root.findViewById(R.id.labelTextView)
        stateTextView = root.findViewById(R.id.stateTextView)
        recyclerView = root.findViewById(R.id.moviesRecyclerView)
        shimmerLayout = root.findViewById(R.id.shimmerLayout)

        recyclerView.apply {
            adapter = moviesAdapter
            addItemDecoration(layoutType.getItemDecoration(context))
            layoutManager = layoutType.getLayoutManger(context)
        }

        if (isInEditMode) {
            // show sample data when shown in the IDE preview
            label = "Upcoming"
            movies = Resource.success(SampleProvider.movieItemList())
        }
    }
}