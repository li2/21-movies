package me.li2.movies.ui.widgets.credits

import android.view.ViewGroup
import me.li2.android.common.arch.Resource
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.CreditListUI
import me.li2.movies.databinding.CreditListViewBinding

class CreditListViewHolder(binding: CreditListViewBinding)
    : BaseViewHolder<Resource<CreditListUI>, CreditListViewBinding>(binding) {

    override fun bind(item: Resource<CreditListUI>, position: Int) {
        binding.casts = item.data?.casts
        binding.crews = item.data?.crews
    }

    companion object {
        fun create(parent: ViewGroup): CreditListViewHolder {
            val binding = newBindingInstance(parent, R.layout.credit_list_view) as CreditListViewBinding
            return CreditListViewHolder(binding)
        }
    }
}