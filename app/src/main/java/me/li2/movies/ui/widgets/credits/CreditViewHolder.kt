/*
 * Created by Weiyi Li on 2020-05-17.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.credits

import android.view.ViewGroup
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.CreditUI
import me.li2.movies.databinding.CreditItemViewBinding

class CreditViewHolder(binding: CreditItemViewBinding)
    : BaseViewHolder<CreditUI, CreditItemViewBinding>(binding) {

    override fun bind(item: CreditUI, position: Int) {
        binding.credit = item
    }

    companion object {
        fun create(parent: ViewGroup): CreditViewHolder {
            return CreditViewHolder(newBindingInstance(parent, R.layout.credit_item_view) as CreditItemViewBinding)
        }
    }
}