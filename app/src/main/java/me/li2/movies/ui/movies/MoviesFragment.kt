package me.li2.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_movies.*
import me.li2.movies.R
import me.li2.movies.arch.Resource.Status.*
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.FragmentMoviesBinding
import me.li2.movies.util.observeOnView
import me.li2.movies.util.toast

class MoviesFragment : BaseFragment() {

    private lateinit var type: MoviesType
    private lateinit var binding: FragmentMoviesBinding
    private val viewModel by activityViewModels<MainViewModel>()
    private val moviesAdapter: MoviesAdapter?
        get() = (rv_movies.adapter as? MoviesAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getParcelable(ARG_KEY_MOVIE_TYPE) ?: MoviesType.COMING_SOON
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        binding.executePendingBindings()

        moviesAdapter?.run {
            compositeDisposable += itemClicks.subscribe {

            }
        }

        srl_movies.setOnRefreshListener { viewModel.getMovies(type) }
    }

    override fun initViewModel() = with(viewModel) { getMovies(type) }

    override fun renderUI() = with(viewModel) {
        observeOnView(if (type == MoviesType.NOT_SHOWING) notShowingMovies else comingSoonMovies) {
            when (it.status) {
                SUCCESS -> bindMovies(it.data, false)
                LOADING -> bindMovies(it.data, true)
                ERROR -> {
                    binding.isLoading = false
                    toast(it.exception.toString())
                }
            }
        }
    }

    private fun bindMovies(movies: List<MovieItem>?, isLoading: Boolean) {
        binding.movies = movies
        binding.isLoading = isLoading
    }

    companion object {
        private const val ARG_KEY_MOVIE_TYPE = "arg_movie_type"

        fun newInstance(type: MoviesType) = MoviesFragment()
                .apply { arguments = bundleOf(ARG_KEY_MOVIE_TYPE to type) }
    }
}
