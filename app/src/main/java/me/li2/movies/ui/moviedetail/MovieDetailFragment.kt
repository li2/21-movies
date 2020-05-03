package me.li2.movies.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.observeOnView
import me.li2.android.common.logic.orFalse
import me.li2.android.common.rx.throttleFirstShort
import me.li2.android.view.navigation.setToolbar
import me.li2.android.view.popup.toast
import me.li2.android.view.system.hideStatusBar
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.MovieDetailFragmentBinding
import me.li2.movies.ui.widgets.moviessummary.MovieSummaryHAdapter
import me.li2.movies.util.RootViewStore
import me.li2.movies.util.navigate
import me.li2.movies.util.watchYoutubeVideo
import timber.log.Timber.e

class MovieDetailFragment : BaseFragment(), RootViewStore {

    private lateinit var binding: MovieDetailFragmentBinding
    private val args by navArgs<MovieDetailFragmentArgs>()
    private val viewModel by viewModels<MovieDetailViewModel>()

    override var rootView: View? = null
    override var hasInitializedRootView: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // load data in your ViewModel's init {} or onCreate(), not in onViewCreated(). 21note
        // https://stackoverflow.com/q/54581071/2722270, https://twitter.com/ianhlake/status/1103522856535638016
        viewModel.getMovieDetailScreenData(args.movieItem.id)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return createRootViewIfNeeded {
            binding = DataBindingUtil.inflate(inflater, R.layout.movie_detail_fragment, container, false)
            binding.root
        }
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        activity?.hideStatusBar()

        initializeRootViewIfNeeded {
            activity?.setToolbar(binding.toolbar)
            binding.executePendingBindings()
            binding.movieItem = args.movieItem
        }

        compositeDisposable += Observable.merge(
                binding.movieOverviewTextView.clicks().throttleFirstShort(),
                binding.movieOverviewExpandTextView.clicks().throttleFirstShort()
        ).subscribe {
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
            navigate(MovieDetailFragmentDirections.showGenreMoviesList(genre.name))
        }

        compositeDisposable += binding.reviewsCountTextView.clicks().throttleFirstShort().subscribe {
            toast("todo: show all reviews")
        }

        compositeDisposable += (binding.recommendationsRecyclerView.adapter as MovieSummaryHAdapter).itemClicks.subscribe { (_, movieItem) ->
            navigate(MovieDetailFragmentDirections.showMovieDetail(movieItem))
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
