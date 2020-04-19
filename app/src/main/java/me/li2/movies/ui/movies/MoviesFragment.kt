package me.li2.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_movies.*
import me.li2.android.common.arch.Resource.Status.*
import me.li2.android.common.arch.observeOnView
import me.li2.android.view.popup.toast
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.FragmentMoviesBinding
import me.li2.movies.ui.movies.MoviesType.COMING_SOON
import me.li2.movies.ui.movies.MoviesType.NOT_SHOWING
import me.li2.movies.util.isTablet
import me.li2.movies.util.navController
import timber.log.Timber.e

class MoviesFragment : BaseFragment() {

    private lateinit var type: MoviesType
    private lateinit var binding: FragmentMoviesBinding
    private val viewModel by activityViewModels<MainViewModel>()
    private val moviesAdapter: MoviesAdapter?
        get() = (rv_movies.adapter as? MoviesAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getParcelable(ARG_KEY_MOVIE_TYPE) ?: COMING_SOON
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
            compositeDisposable += movieClicks.subscribe { (imageView, movieItem) ->
                val extras = FragmentNavigatorExtras(imageView to getString(R.string.transition_name_movie) + movieItem.id)
                if (this@MoviesFragment.isTablet()) {
                    nav_host_land?.navController()?.navigate(R.id.movieDetailFragment, bundleOf("movieItem" to movieItem), null, extras)
                } else {
                    navController().navigate(MainFragmentDirections.showMovieDetail(movieItem), extras)
                }
            }
        }

        srl_movies.setOnRefreshListener {
            viewModel.getMovies(type, true)
            viewModel.getUpcomingMovies()
        }
    }

    override fun initViewModel() = with(viewModel) {
        getMovies(type)
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(if (type == NOT_SHOWING) notShowingMovies else comingSoonMovies) {
            when (it.status) {
                LOADING -> bindMovies(it.data, true)
                SUCCESS -> bindMovies(it.data, false)
                ERROR -> {
                    binding.isLoading = false
                    toast(it.exception.toString())
                    e(it.exception, "failed to get movies")
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
