/*
 * Created by Weiyi on 2020-05-25.
 * https://github.com/li2
 */
package me.li2.movies.ui.widgets.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import me.li2.android.view.image.GlideRequestListener
import me.li2.android.view.navigation.setToolbar
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.BigImageFragmentBinding
import me.li2.movies.util.ContainerTransformConfiguration
import me.li2.movies.util.setUpContainerEnterTransitions
import javax.inject.Inject

class BigImageFragment : BaseFragment() {

    private lateinit var binding: BigImageFragmentBinding
    private val args by navArgs<BigImageFragmentArgs>()

    @Inject
    lateinit var containerTransformConfiguration: ContainerTransformConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpContainerEnterTransitions(containerTransformConfiguration)
        postponeEnterTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.big_image_fragment, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        activity?.setToolbar(binding.toolbar)
        ViewCompat.setTransitionName(binding.root, args.imageUrl)

        binding.imageUrl = args.imageUrl

        binding.onGlideRequestComplete = object : GlideRequestListener {
            override fun onGlideRequestComplete(success: Boolean) {
                startPostponedEnterTransition()
            }
        }
    }
}