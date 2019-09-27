package me.li2.movies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.FragmentMainBinding
import kotlin.random.Random

class MainFragment : BaseFragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        binding.title = Random(42).nextInt().toString()
    }
}
