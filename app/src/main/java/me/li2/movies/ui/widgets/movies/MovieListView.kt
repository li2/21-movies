/*
 * Created by Weiyi Li on 2020-05-18.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.movies

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.subjects.PublishSubject
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.*
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.ui.widgets.movies.MovieListLayoutType.LINEAR_LAYOUT_HORIZONTAL
import me.li2.movies.util.SampleProvider
import me.li2.movies.util.noData
import me.li2.movies.util.showAnimation

class MovieListView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val labelTextView: TextView
    private val moreButton: Button
    private val recyclerView: RecyclerView
    private val stateLayout: View
    private val stateTextView: TextView
    private val shimmerLayout: ShimmerFrameLayout

    private val moviesAdapter = MovieListAdapter(LINEAR_LAYOUT_HORIZONTAL)

    private val _onMoreClicks = PublishSubject.create<Unit>()
    val onMoreClicks = _onMoreClicks.toFlowable(BackpressureStrategy.LATEST).toObservable()!!

    val onMovieClicks = moviesAdapter.onMovieClicks

    var label: String? = null
        set(value) {
            if (field == value) return
            field = value
            labelTextView.text = value
            labelTextView.isVisible = value != null
        }

    var movies: Resource<List<MovieItemUI>>? = null
        set(value) {
            if (value == null || field == value) return
            field = value
            // update list
            moviesAdapter.submitList(value.data)
            moreButton.isVisible = !value.data.isNullOrEmpty()
            // update loading state
            shimmerLayout.showAnimation(value.status == LOADING && value.noData())
            // update empty or error message
            val stateMessage = when {
                value.status == SUCCESS && value.noData() -> "oops no movies yet"
                value.status == ERROR && value.noData() -> "\uD83D\uDE28 Wooops ${value.exception?.message}"
                else -> null
            }
            stateTextView.text = stateMessage
            stateLayout.isVisible = stateMessage != null
        }

    var layoutType: MovieListLayoutType = LINEAR_LAYOUT_HORIZONTAL
        set(value) {
            if (field == value) return
            field = value
            moviesAdapter.layoutType = value
        }

    init {
        val root = inflate(context, R.layout.movie_list_view, this)
        labelTextView = root.findViewById(R.id.labelTextView)
        moreButton = root.findViewById(R.id.moreButton)
        recyclerView = root.findViewById(R.id.moviesRecyclerView)
        stateTextView = root.findViewById(R.id.stateTextView)
        stateLayout = root.findViewById(R.id.stateLayout)
        shimmerLayout = root.findViewById(R.id.shimmerLayout)

        /* Custom view must have a unique ID, otherwise, when saving state,
        all of them are being saves in same state, and the last one overwrites all.
        therefor the recycler scroll position restored to 0.
        A simple fix is just use View.generateViewId(). noteweiyi*/
        labelTextView.id = View.generateViewId()
        moreButton.id = View.generateViewId()
        recyclerView.id = View.generateViewId()
        stateLayout.id = View.generateViewId()
        stateTextView.id = View.generateViewId()
        shimmerLayout.id = View.generateViewId()

        recyclerView.apply {
            adapter = moviesAdapter
            addItemDecoration(layoutType.getItemDecoration(context))
            layoutManager = layoutType.getLayoutManger(context)
        }

        moreButton.clicks().throttleFirstShort().subscribe(_onMoreClicks)

        showSampleData()
    }

    /** show sample data when shown in the IDE preview */
    private fun showSampleData() {
        if (isInEditMode) {
            label = "Upcoming"
            movies = Resource.success(SampleProvider.movieItemList())
        }
    }

/*
    private val PARCEL_KEY_STATE = "instanceState"
    private val PARCEL_KEY_RECYCLER_VIEW_STATE = "recycler_view_state"

    override fun onSaveInstanceState(): Parcelable? {
        return Bundle().apply {
            putParcelable(PARCEL_KEY_STATE, super.onSaveInstanceState())
            putParcelable(PARCEL_KEY_RECYCLER_VIEW_STATE, recyclerView.layoutManager?.onSaveInstanceState())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var restoredState = state
        (restoredState as? Bundle)?.let { bundle ->
            recyclerView.layoutManager?.onRestoreInstanceState(bundle.getParcelable(PARCEL_KEY_RECYCLER_VIEW_STATE))
            restoredState = bundle.getParcelable(PARCEL_KEY_STATE)
        }
        super.onRestoreInstanceState(restoredState)
    }
*/
}