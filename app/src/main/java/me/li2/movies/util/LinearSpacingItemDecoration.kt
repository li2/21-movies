package me.li2.movies.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class LinearSpacingItemDecoration(private val context: Context,
                                  @DimenRes private val sizeResId: Int,
                                  @RecyclerView.Orientation val orientation: Int)
    : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        val space = context.resources.getDimensionPixelSize(sizeResId)
        when (orientation) {
            RecyclerView.VERTICAL -> outRect.bottom = space
            RecyclerView.HORIZONTAL -> outRect.right = space
        }
    }
}
