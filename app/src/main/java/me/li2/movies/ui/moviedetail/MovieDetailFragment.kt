package me.li2.movies.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxkotlin.plusAssign
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.observeOnView
import me.li2.android.common.number.dpToPx
import me.li2.android.view.list.LinearSpacingDecoration
import me.li2.android.view.navigation.setToolbar
import me.li2.android.view.popup.toast
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.MovieDetailFragmentBinding
import me.li2.movies.util.RootViewStore
import me.li2.movies.util.enforceSingleScrollDirection
import me.li2.movies.util.navigate
import timber.log.Timber.e

class MovieDetailFragment : BaseFragment(), RootViewStore {

    private lateinit var binding: MovieDetailFragmentBinding
    private val args by navArgs<MovieDetailFragmentArgs>()
    private val viewModel by viewModels<MovieDetailViewModel>()

    override var rootView: View? = null
    override var hasInitializedRootView: Boolean = false

    private val detailAdapter = MovieDetailAdapter()

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
        initializeRootViewIfNeeded {
            activity?.setToolbar(binding.toolbar)
            binding.movieItem = args.movieItem
            binding.recyclerView.apply {
                adapter = detailAdapter
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(LinearSpacingDecoration(RecyclerView.VERTICAL, 32.dpToPx(context)))
                enforceSingleScrollDirection()
            }
            viewModel.movieItem = args.movieItem
        }

        compositeDisposable += detailAdapter.onRateClicks.subscribe {
            toast("todo: rate movie clicks")
        }

        compositeDisposable += detailAdapter.onGenreClicks.subscribe { genre ->
            navigate(MovieDetailFragmentDirections.showGenreMoviesList(genre.name))
        }

        compositeDisposable += detailAdapter.onRecMovieClicks.subscribe { (_, movieItem) ->
            navigate(MovieDetailFragmentDirections.showMovieDetail(movieItem))
        }
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(movieDetailRows) {
            detailAdapter.submitList(it)
        }

        observeOnView(movieDetail) {
            bindLoadingStatus(it)
        }

        observeOnView(movieReviews) {
            bindLoadingStatus(it)
        }

        observeOnView(movieTrailer) {
            bindLoadingStatus(it)
        }

        observeOnView(recommendations) {
            bindLoadingStatus(it)
        }
    }

    private fun bindLoadingStatus(resource: Resource<*>) {
        if (resource.status == Resource.Status.ERROR) {
            toast(resource.exception.toString())
            e(resource.exception)
        }
    }
}
