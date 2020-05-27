/*
 * Created by Weiyi Li on 2020-05-17.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.credits

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.subjects.PublishSubject
import me.li2.movies.data.model.CreditUI

class CreditListAdapter(private val onCreditClicks: PublishSubject<Pair<View, CreditUI>>)
    : RecyclerView.Adapter<CreditViewHolder>() {

    var credits: List<CreditUI> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditViewHolder {
        return CreditViewHolder.create(parent, onCreditClicks)
    }

    override fun onBindViewHolder(viewHolder: CreditViewHolder, position: Int) {
        viewHolder.bind(credits[position], position)
    }

    override fun getItemCount() = credits.size
}