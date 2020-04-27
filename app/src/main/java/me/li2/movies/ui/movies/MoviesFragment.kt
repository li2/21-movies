package me.li2.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.MoviesFragmentBinding

class MoviesFragment : BaseFragment() {

    private lateinit var binding: MoviesFragmentBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.movies_fragment, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        binding.executePendingBindings()
    }
}
