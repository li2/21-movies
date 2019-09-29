package me.li2.movies.util

import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

object ViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setViewVisibility(view: View, value: Boolean?) {
        view.visibility = if (value.orFalse()) VISIBLE else GONE
    }

    @JvmStatic
    @BindingAdapter("app:isRefreshing")
    fun showLoadingSwipeRefreshLayout(srl: SwipeRefreshLayout, value: Boolean) {
        srl.isRefreshing = value
    }

    @JvmStatic
    @BindingAdapter("app:collapsingScrollEnabled")
    fun setCollapsingToolbarLayoutScrollEnabled(collapsingToolbarLayout: CollapsingToolbarLayout, enabled: Boolean?) {
        val lp = collapsingToolbarLayout.layoutParams as AppBarLayout.LayoutParams
        if (enabled.orFalse()) {
            lp.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
        } else {
            lp.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }
        collapsingToolbarLayout.layoutParams = lp
    }

    @JvmStatic
    @BindingAdapter(value = ["android:src", "placeHolder", "centerCrop", "fitCenter"], requireAll = false)
    fun setImageUrl(view: ImageView,
                    src: String?,
                    placeHolder: Drawable?,
                    centerCrop: Boolean,
                    fitCenter: Boolean) {
        val requestOptions = RequestOptions().apply {
            if (centerCrop) centerCrop()
            if (fitCenter) fitCenter()
            if (placeHolder != null) placeholder(placeHolder)
        }
        if (src != null) {
            Glide.with(view.context)
                    .load(src)
                    .apply(requestOptions)
                    .into(view)
        }
    }
}
