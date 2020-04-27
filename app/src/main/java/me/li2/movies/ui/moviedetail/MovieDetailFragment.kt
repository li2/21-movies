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
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.observeOnView
import me.li2.android.common.logic.orFalse
import me.li2.android.common.rx.throttleFirstShort
import me.li2.android.view.popup.toast
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.MovieDetailFragmentBinding
import me.li2.movies.util.ifSupportLollipop
import me.li2.movies.util.navController
import me.li2.movies.util.watchYoutubeVideo
import timber.log.Timber.e

class MovieDetailFragment : BaseFragment() {

    private lateinit var binding: MovieDetailFragmentBinding
    private val args by navArgs<MovieDetailFragmentArgs>()
    private val viewModel by viewModels<MovieDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // load data in your ViewModel's init {} or onCreate(), not in onViewCreated(). 21note
        // https://twitter.com/i/status/1103510156741095425
        viewModel.getMovieDetailScreenData(args.movieItem.id)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.movie_detail_fragment, container, false)
        return binding.root
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        binding.executePendingBindings()
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
            toast("todo: rate movie clicks")
        }

        compositeDisposable += binding.genresGroupView.genreClicks().subscribe { genre ->
            navController().navigate(MovieDetailFragmentDirections.showGenreMoviesList(genre.name))
        }

        compositeDisposable += binding.reviewsCountTextView.clicks().throttleFirstShort().subscribe {
            toast("todo: show all reviews")
        }
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(movieDetailLiveData) {
            binding.movieDetail = it.data
            bindLoadingStatus(it)
        }

        observeOnView(movieReviewsLiveData) {
            binding.reviews = it.data?.reviews?.take(MAXIMUM_REVIEWS)
            binding.reviewsCount = it.data?.totalResults
            bindLoadingStatus(it)
        }

        observeOnView(youtubeUrlLiveData) {
            binding.youtubeUrl = it.data
            bindLoadingStatus(it)
        }

        observeOnView(recommendationsLiveData) {
            binding.recommendations = it.data
            bindLoadingStatus(it)
        }
    }

    private fun bindLoadingStatus(resource: Resource<*>) {
        if (resource.status == Resource.Status.ERROR) {
            toast(resource.exception.toString())
            e(resource.exception)
        }
    }

    companion object {
        const val MAXIMUM_REVIEWS = 4
    }
}
