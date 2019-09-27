package me.li2.movies.util

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.BindingAdapter

object ViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setViewVisibility(view: View, value: Boolean?) {
        view.visibility = if (value.orFalse()) VISIBLE else GONE
    }
}
