/*
 * Created by Weiyi Li on 2020-05-17.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.credits

import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.subjects.PublishSubject
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.CreditUI
import me.li2.movies.databinding.CreditItemViewBinding

class CreditViewHolder(binding: CreditItemViewBinding,
                       private val onCreditClicks: PublishSubject<Pair<View, CreditUI>>)
    : BaseViewHolder<CreditUI, CreditItemViewBinding>(binding) {

    override fun bind(item: CreditUI, position: Int) {
        binding.credit = item
        binding.creditItemContainer.clicks().throttleFirstShort()
                .map { Pair(binding.creditItemContainer, item) }
                .subscribe(onCreditClicks)
    }

    companion object {
        fun create(parent: ViewGroup,
                   onCreditClicks: PublishSubject<Pair<View, CreditUI>>): CreditViewHolder {
            return CreditViewHolder(newBindingInstance(parent, R.layout.credit_item_view) as CreditItemViewBinding, onCreditClicks)
        }
    }
}