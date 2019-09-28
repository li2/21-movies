package me.li2.movies.ui.movies

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import me.li2.movies.R

class MoviesTabAdapter(private val context: Context, fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int) = when (position) {
        0 -> MoviesFragment.newInstance(MoviesTabType.NOT_SHOWING)
        1 -> MoviesFragment.newInstance(MoviesTabType.COMING_SOON)
        else -> throw IllegalStateException("Index unsupported")
    }

    override fun getPageTitle(position: Int): String = when (position) {
        0 -> context.getString(R.string.not_showing)
        1 -> context.getString(R.string.comming_soon)
        else -> throw IllegalStateException("Index unsupported")
    }

    override fun getCount() = 2
}
