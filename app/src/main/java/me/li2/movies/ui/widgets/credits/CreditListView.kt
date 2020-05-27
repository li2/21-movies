/*
 * Created by Weiyi Li on 2020-05-17.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.credits

import android.view.View
import android.view.ViewGroup
import io.reactivex.rxjava3.subjects.PublishSubject
import me.li2.android.common.arch.Resource
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.CreditListUI
import me.li2.movies.data.model.CreditUI
import me.li2.movies.databinding.CreditListViewBinding

class CreditListViewHolder(binding: CreditListViewBinding,
                           onCreditClicks: PublishSubject<Pair<View, CreditUI>>)
    : BaseViewHolder<Resource<CreditListUI>, CreditListViewBinding>(binding) {

    init {
        binding.onCreditClicks = onCreditClicks
    }

    override fun bind(item: Resource<CreditListUI>, position: Int) {
        binding.casts = item.data?.casts
        binding.crews = item.data?.crews
    }

    companion object {
        fun create(parent: ViewGroup,
                   onCreditClicks: PublishSubject<Pair<View, CreditUI>>): CreditListViewHolder {
            val binding = newBindingInstance(parent, R.layout.credit_list_view) as CreditListViewBinding
            return CreditListViewHolder(binding, onCreditClicks)
        }
    }
}