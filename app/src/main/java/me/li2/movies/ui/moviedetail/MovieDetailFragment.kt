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
import io.reactivex.rxjava3.kotlin.plusAssign
import me.li2.android.common.arch.observeOnView
import me.li2.android.common.framework.openUrl
import me.li2.android.common.number.dpToPx
import me.li2.android.view.image.GlideRequestListener
import me.li2.android.view.list.LinearSpacingDecoration
import me.li2.android.view.list.enforceSingleScrollDirection
import me.li2.android.view.navigation.setToolbar
import me.li2.android.view.popup.toast
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.databinding.MovieDetailFragmentBinding
import me.li2.movies.ui.movies.RecommendationCategory
import me.li2.movies.util.*
import javax.inject.Inject

class MovieDetailFragment : BaseFragment(), RootViewStore {

    private lateinit var binding: MovieDetailFragmentBinding
    private val args by navArgs<MovieDetailFragmentArgs>()

    @Inject
    lateinit var viewModelAssistedFactory: MovieDetailViewModelAssistedFactory

    private val viewModel by viewModels<MovieDetailViewModel> {
        viewModelAssistedFactory.create(args.movieItem)
    }

    override var rootView: View? = null
    override var hasInitializedRootView: Boolean = false
    override var hasInitializedOptionsMenu: Boolean = false

    private val detailAdapter = MovieDetailAdapter()

    @Inject
    lateinit var containerTransformConfiguration: ContainerTransformConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpContainerEnterTransitions(containerTransformConfiguration)

        // load data in your ViewModel's init {} or onCreate(), not in onViewCreated(). 21note))
        // https://stackoverflow.com/q/54581071/2722270, https://twitter.com/ianhlake/status/1103522856535638016
        viewModel.getMovieDetailScreenData(args.movieItem.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        binding.onGlideRequestComplete = object : GlideRequestListener {
            override fun onGlideRequestComplete(success: Boolean) {
                if (!success) {
                    binding.appBarLayout.setExpanded(false)
                }
            }
        }

        compositeDisposable += detailAdapter.onSaveClicks.subscribe {
            viewModel.toggleWatchlist(it) { isInWatchlist ->
                if (isInWatchlist) {
                    toast("movie added in local database")
                } else {
                    toast("movie removed from local database")
                }
            }
        }

        compositeDisposable += detailAdapter.onTrailerClicks.subscribe { trailer ->
            requireContext().openUrl(trailer.url)
        }

        compositeDisposable += detailAdapter.onPosterClicks.subscribe { (view, posterUrl) ->
            setUpContainerExitTransition(R.id.root)
            val extras = FragmentNavigatorExtras(view to ViewCompat.getTransitionName(view).orEmpty())
            navController().navigate(MovieDetailFragmentDirections.showBigImage(posterUrl), extras)
        }

        compositeDisposable += detailAdapter.onCreditClicks.subscribe { (view, credit) ->
            credit.profileOriginalUrl?.let { profileImageUrl ->
                setUpContainerExitTransition(R.id.root)
                val extras = FragmentNavigatorExtras(view to ViewCompat.getTransitionName(view).orEmpty())
                navController().navigate(MovieDetailFragmentDirections.showBigImage(profileImageUrl), extras)
            }
        }

        compositeDisposable += detailAdapter.onCategoryClicks.subscribe {
            removeContainerExitTransition()
            navigateSlideInOut(MovieDetailFragmentDirections.showMoviesList(it))
        }

        compositeDisposable += detailAdapter.onRecMovieClicks.subscribe { (view, movieItem) ->
            setUpContainerExitTransition(R.id.root)
            val extras = FragmentNavigatorExtras(view to ViewCompat.getTransitionName(view).orEmpty())
            navController().navigate(MovieDetailFragmentDirections.showMovieDetail(movieItem), extras)
        }

        compositeDisposable += detailAdapter.onMoreRecClicks.subscribe {
            removeContainerExitTransition()
            navigateSlideInOut(MovieDetailFragmentDirections.showMoviesList(RecommendationCategory(args.movieItem.id)))
        }
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(movieDetailRows) {
            detailAdapter.submitList(it)
        }
    }
}
