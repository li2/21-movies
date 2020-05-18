/*
 * Created by Weiyi Li on 2020-05-18.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.movies

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.li2.android.common.number.dpToPx
import me.li2.android.view.list.DividerItemDecoration
import me.li2.android.view.list.GridSpacingDecoration
import me.li2.android.view.list.LinearSpacingDecoration
import me.li2.movies.R

enum class MovieListLayoutType {
    LINEAR_LAYOUT_HORIZONTAL,
    LINEAR_LAYOUT_VERTICAL,
    GRID_LAYOUT;

    @LayoutRes
    fun getLayoutResId(): Int {
        return if (this == LINEAR_LAYOUT_VERTICAL) {
            R.layout.movie_item_vertical_view
        } else {
            R.layout.movie_item_horizontal_view
        }
    }

    fun getLayoutManger(context: Context): RecyclerView.LayoutManager {
        return when (this) {
            LINEAR_LAYOUT_HORIZONTAL -> LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            LINEAR_LAYOUT_VERTICAL -> LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            GRID_LAYOUT -> GridLayoutManager(context, SPAN_COUNT, RecyclerView.VERTICAL, false)
        }
    }

    fun getItemDecoration(context: Context): RecyclerView.ItemDecoration {
        return when (this) {
            LINEAR_LAYOUT_HORIZONTAL -> LinearSpacingDecoration(RecyclerView.HORIZONTAL, 16.dpToPx(context))
            LINEAR_LAYOUT_VERTICAL -> DividerItemDecoration(context, RecyclerView.VERTICAL)
            GRID_LAYOUT -> GridSpacingDecoration(SPAN_COUNT, 16.dpToPx(context))
        }
    }

    companion object {
        private const val SPAN_COUNT = 3
    }
}