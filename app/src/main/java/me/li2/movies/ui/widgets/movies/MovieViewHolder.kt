/*
 * Created by Weiyi Li on 2020-05-18.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.subjects.PublishSubject
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.databinding.MovieItemHorizontalViewBinding
import me.li2.movies.databinding.MovieItemVerticalViewBinding

class MovieViewHolder(root: View,
                      private val binding: ViewDataBinding?,
                      private val layoutType: MovieListLayoutType,
                      private val itemClicks: PublishSubject<Pair<ImageView, MovieItemUI>>)
    : RecyclerView.ViewHolder(root) {

    fun bind(item: MovieItemUI) {
        if (layoutType == MovieListLayoutType.LINEAR_LAYOUT_VERTICAL) {
            (binding as? MovieItemVerticalViewBinding)?.let {
                binding.item = item
                binding.root
                        .clicks()
                        .throttleFirstShort()
                        .map { Pair(binding.posterImageView, item) }
                        .subscribe(itemClicks)
            }
        } else {
            (binding as? MovieItemHorizontalViewBinding)?.let {
                binding.item = item
                binding.root
                        .clicks()
                        .throttleFirstShort()
                        .map { Pair(binding.posterImageView, item) }
                        .subscribe(itemClicks)
            }
        }
    }

    // Custom view with data-binding cannot be rendered by layout preview due to NullPointerException
    // at data-binding.TheCustomViewBindingImpl.<init> notewy
    // Solution:
    // 1. Use View.isInEditMode() in your custom views to skip code or show sample data when shown in the IDE.
    // 2. DON'T use data-binding in custom view, inflate the layout and findViewById;
    //      as for the <include> layout, make sure your id of <include> is the same as the root element in your included XML file.
    //      otherwise, IllegalStateException: root.findViewById(R.id.shimmerLayout) must not be null
    companion object {
        fun create(parent: ViewGroup,
                   layoutType: MovieListLayoutType,
                   itemClicks: PublishSubject<Pair<ImageView, MovieItemUI>>): MovieViewHolder {
            val root = LayoutInflater.from(parent.context).inflate(layoutType.getLayoutResId(), parent, false)
            val binding = if (!parent.isInEditMode) {
                DataBindingUtil.bind<ViewDataBinding>(root)
            } else {
                null
            }
            return MovieViewHolder(root, binding, layoutType, itemClicks)
        }
    }
}