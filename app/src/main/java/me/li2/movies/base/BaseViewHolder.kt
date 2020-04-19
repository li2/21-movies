package me.li2.movies.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T, VB : ViewDataBinding>(val binding: VB)
    : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: T, position: Int)

    companion object {
        fun newBindingInstance(parent: ViewGroup, @LayoutRes layoutRes: Int): ViewDataBinding {
            return DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutRes, parent, false)
        }
    }
}
