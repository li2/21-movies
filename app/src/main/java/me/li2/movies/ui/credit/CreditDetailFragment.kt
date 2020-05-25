/*
 * Created by Weiyi on 2020-05-25.
 * https://github.com/li2
 */
package me.li2.movies.ui.credit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import me.li2.android.view.navigation.setToolbar
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.CreditDetailFragmentBinding

class CreditDetailFragment : BaseFragment() {

    private lateinit var binding: CreditDetailFragmentBinding
    private val args by navArgs<CreditDetailFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.credit_detail_fragment, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        activity?.setToolbar(binding.toolbar)
        binding.credit = args.credit
    }
}