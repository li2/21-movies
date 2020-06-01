/*
 * Created by Weiyi Li on 2020-05-18.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.trailers

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.subjects.PublishSubject
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.Resource.Status.SUCCESS
import me.li2.movies.R
import me.li2.movies.base.BaseViewHolder
import me.li2.movies.data.model.Trailer
import me.li2.movies.databinding.TrailerListViewholderBinding

class TrailerListViewHolder(binding: TrailerListViewholderBinding,
                            onTrailerClicks: PublishSubject<Trailer>)
    : BaseViewHolder<Resource<List<Trailer>>, TrailerListViewholderBinding>(binding) {

    private val trailersAdapter = TrailerListAdapter(onTrailerClicks)

    init {
        binding.trailersRecyclerView.apply {
            adapter = trailersAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
    }

    override fun bind(item: Resource<List<Trailer>>, position: Int) {
        binding.visible = item.status == SUCCESS && !item.data.isNullOrEmpty()
        trailersAdapter.submitList(item.data)
    }

    companion object {
        fun create(parent: ViewGroup,
                   onTrailerClicks: PublishSubject<Trailer>): TrailerListViewHolder {
            val binding = newBindingInstance(parent, R.layout.trailer_list_viewholder) as TrailerListViewholderBinding
            return TrailerListViewHolder(binding, onTrailerClicks)
        }
    }
}