package me.li2.movies.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import me.li2.android.common.arch.observeOnView
import me.li2.android.common.logic.orFalse
import me.li2.android.common.rx.throttleFirstShort
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.MovieDetailFragmentBinding
import me.li2.movies.util.ifSupportLollipop
import me.li2.movies.util.themedSnackbar
import me.li2.movies.util.watchYoutubeVideo

class MovieDetailFragment : BaseFragment() {

    private lateinit var binding: MovieDetailFragmentBinding
    private val viewModel by viewModels<MovieDetailViewModel>()
    private val args by navArgs<MovieDetailFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.movie_detail_fragment, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        binding.movieItem = args.movieItem

        ifSupportLollipop {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.explode)
            ViewCompat.setTransitionName(binding.posterImageView, getString(R.string.transition_name_movie) + args.movieItem.id)
        }

        compositeDisposable += Observable.merge(binding.movieOverviewTextView.clicks().throttleFirstShort(),
                binding.movieOverviewExpandTextView.clicks().throttleFirstShort())
                .subscribe {
                    binding.isOverviewExpanded = !binding.isOverviewExpanded.orFalse()
                }

        compositeDisposable += binding.youtubeButton.clicks().throttleFirstShort().subscribe {
            binding.youtubeUrl?.let {
                requireContext().watchYoutubeVideo(it)
            }
        }

        compositeDisposable += binding.rateMovieButton.clicks().throttleFirstShort().subscribe {
            themedSnackbar("todo: rate movie clicks")
        }
    }

    override fun initViewModel() = with(viewModel) {
        val movieId = args.movieItem.id
        getMovieDetail(movieId)
        getMovieReviews(movieId)
        getYouTubeUrl(movieId)
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(movieDetailLiveData) {
            binding.movieDetail = it.data
        }

        observeOnView(youtubeUrlLiveData) {
            binding.youtubeUrl = it.data
        }
    }
}
