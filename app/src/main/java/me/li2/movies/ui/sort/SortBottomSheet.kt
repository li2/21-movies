/*
 * Created by Weiyi on 2020-05-30.
 * https://github.com/li2
 */
package me.li2.movies.ui.sort

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.li2.movies.R

class SortBottomSheet @JvmOverloads constructor(
        context: Context,
        defStyleAttr: Int = 0,
        sortItems: List<SortItemUI>,
        onSortChange: (MoviesSortType, Boolean) -> Unit)
    : BottomSheetDialog(context, defStyleAttr) {

    private var recyclerView: RecyclerView
    private val adapter = SortAdapter(onSortChange)

    init {
        setContentView(R.layout.sort_bottom_sheet)
        dismissWithAnimation = true
        recyclerView = findViewById(R.id.sortItemsRecyclerView)!!
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.submitList(sortItems)
    }
}
