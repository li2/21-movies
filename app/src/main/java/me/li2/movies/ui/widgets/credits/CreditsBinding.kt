/*
 * Created by Weiyi Li on 2020-05-17.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.credits

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import io.reactivex.rxjava3.subjects.PublishSubject
import me.li2.android.common.number.dpToPx
import me.li2.android.view.list.LinearSpacingDecoration
import me.li2.movies.data.model.CreditUI

object CreditsBinding {
    @JvmStatic
    @BindingAdapter(value = ["credits", "creditClicks"], requireAll = true)
    fun setCredits(rv: RecyclerView,
                   items: List<CreditUI>?,
                   onCreditClicks: PublishSubject<Pair<View, CreditUI>>) {
        if (rv.adapter as? CreditListAdapter == null) {
            rv.adapter = CreditListAdapter(onCreditClicks)
            rv.layoutManager = LinearLayoutManager(rv.context, HORIZONTAL, false)
            rv.addItemDecoration(LinearSpacingDecoration(HORIZONTAL, 8.dpToPx(rv.context)))
        }
        (rv.adapter as CreditListAdapter).credits = items.orEmpty()
    }
}