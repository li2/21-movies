package me.li2.movies.ui.widgets.credits

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import me.li2.android.common.number.dpToPx
import me.li2.android.view.list.LinearSpacingDecoration
import me.li2.movies.data.model.CreditUI

object CreditsBinding {
    @JvmStatic
    @BindingAdapter(value = ["app:credits"])
    fun setCredits(rv: RecyclerView, items: List<CreditUI>?) {
        if (rv.adapter as? CreditListAdapter == null) {
            rv.adapter = CreditListAdapter()
            rv.layoutManager = LinearLayoutManager(rv.context, HORIZONTAL, false)
            rv.addItemDecoration(LinearSpacingDecoration(HORIZONTAL, 8.dpToPx(rv.context)))
        }
        (rv.adapter as CreditListAdapter).credits = items.orEmpty()
    }
}