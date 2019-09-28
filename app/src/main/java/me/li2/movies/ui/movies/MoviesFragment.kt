package me.li2.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.FragmentMoviesBinding
import kotlin.random.Random

class MoviesFragment : BaseFragment() {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var type: MoviesTabType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getParcelable(ARG_KEY_MOVIE_TYPE) ?: MoviesTabType.COMING_SOON
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        binding.title = Random(42).nextInt().toString()
    }

    companion object {
        private const val ARG_KEY_MOVIE_TYPE = "arg_movie_type"

        fun newInstance(type: MoviesTabType) = MoviesFragment()
                .apply { arguments = bundleOf(ARG_KEY_MOVIE_TYPE to type) }
    }
}
