package me.li2.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import io.reactivex.rxkotlin.plusAssign
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.observeOnView
import me.li2.android.view.popup.toast
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.MoviesFragmentBinding
import me.li2.movies.ui.moviedetail.MovieDetailViewModel
import me.li2.movies.ui.widgets.moviessummary.MovieSummaryVAdapter
import me.li2.movies.util.navigate
import me.li2.movies.util.setToolbarTitle
import timber.log.Timber

class MoviesFragment : BaseFragment() {

    private lateinit var binding: MoviesFragmentBinding
    private val args by navArgs<MoviesFragmentArgs>()
    private val viewModel by viewModels<MovieDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.searchGenreMovies(args.genre)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.movies_fragment, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        binding.executePendingBindings()
        setToolbarTitle(args.genre)

        compositeDisposable += (binding.moviesRecyclerView.adapter as MovieSummaryVAdapter).itemClicks.subscribe { (_, movieItem) ->
            navigate(MoviesFragmentDirections.showMovieDetail(movieItem))
        }
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(genreMoviesLiveData) {
            binding.movies = it.data
            bindLoadingStatus(it)
        }
    }

    private fun bindLoadingStatus(resource: Resource<*>) {
        if (resource.status == Resource.Status.ERROR) {
            toast(resource.exception.toString())
            Timber.e(resource.exception)
        }
    }
}
