/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Completable
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
import me.li2.movies.util.*
import org.kodein.di.generic.instance
import timber.log.Timber.e
import java.util.concurrent.TimeUnit

class MovieDetailFragment : BaseFragment(), RootViewStore {

    private lateinit var binding: MovieDetailFragmentBinding
    private val args by navArgs<MovieDetailFragmentArgs>()
    private val viewModel by viewModels<MovieDetailViewModel> { MovieDetailViewModelFactory(args.movieItem) }

    override var rootView: View? = null
    override var hasInitializedRootView: Boolean = false

    private val detailAdapter = MovieDetailAdapter()
    private val containerTransformConfiguration by instance<ContainerTransformConfiguration>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpContainerEnterTransitions(containerTransformConfiguration)
        // load data in your ViewModel's init {} or onCreate(), not in onViewCreated(). 21note))
        // https://stackoverflow.com/q/54581071/2722270, https://twitter.com/ianhlake/status/1103522856535638016
        compositeDisposable += Completable.complete()
                .delay(500, TimeUnit.MICROSECONDS)
                .doOnComplete {
                    // delay loading data to make transition smooth, ideally make the call onTransitionEnd, however its not called.
                    viewModel.getMovieDetailScreenData(args.movieItem.id)
                }
                .subscribe()
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

            // Set the transition name which matches the list/grid item to be transitioned from for
            // the shared element transition.
            ViewCompat.setTransitionName(binding.root, args.movieItem.getSharedTransitionName())
        }

        compositeDisposable += detailAdapter.onRateClicks.subscribe {
            toast("todo: rate movie clicks")
        }

        compositeDisposable += detailAdapter.onGenreClicks.subscribe { genre ->
            navigate(MovieDetailFragmentDirections.showGenreMoviesList(genre.name))
        }

        compositeDisposable += detailAdapter.onRecMovieClicks.subscribe { (view, movieItem) ->
            setUpContainerExitTransition(R.id.root)
            val extras = FragmentNavigatorExtras(view to ViewCompat.getTransitionName(view).orEmpty())
            navController().navigate(MovieDetailFragmentDirections.showMovieDetail(movieItem), extras)
        }
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(movieDetailRows) {
            detailAdapter.submitList(it)
        }

        observeOnView(movieDetail) {
            bindErrorState(it)
        }

        observeOnView(credits) {
            bindErrorState(it)
        }

        observeOnView(movieReviews) {
            bindErrorState(it)
        }

        observeOnView(movieTrailer) {
            bindErrorState(it)
        }

        observeOnView(recommendations) {
            bindErrorState(it)
        }
    }

    private fun bindErrorState(resource: Resource<*>) {
        if (resource.status == Resource.Status.ERROR) {
            toast(resource.exception.toString())
            e(resource.exception)
        }
    }
}
