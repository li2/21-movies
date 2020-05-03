package me.li2.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxkotlin.plusAssign
import me.li2.android.common.arch.observeOnView
import me.li2.android.common.rx.throttleFirstShort
import me.li2.android.view.list.DividerItemDecoration
import me.li2.android.view.list.onScrolledBottom
import me.li2.movies.R
import me.li2.movies.base.BaseFragment
import me.li2.movies.data.model.MapperUI
import me.li2.movies.databinding.MoviesFragmentBinding
import me.li2.movies.ui.moviedetail.MovieDetailViewModel
import me.li2.movies.ui.widgets.moviessummary.MovieSummaryVAdapter
import me.li2.movies.ui.widgets.paging.PagingItemAdapter
import me.li2.movies.util.navigate
import me.li2.movies.util.setToolbarTitle

class MoviesFragment : BaseFragment() {

    private lateinit var binding: MoviesFragmentBinding
    private val args by navArgs<MoviesFragmentArgs>()
    private val viewModel by viewModels<MovieDetailViewModel>()

    private val moviesAdapter = MovieSummaryVAdapter()
    private val pagingAdapter = PagingItemAdapter()

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

        binding.moviesRecyclerView.apply {
            adapter = MergeAdapter(moviesAdapter, pagingAdapter)
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

        compositeDisposable += moviesAdapter.itemClicks.throttleFirstShort().subscribe { (_, movieItem) ->
            navigate(MoviesFragmentDirections.showMovieDetail(movieItem))
        }

        compositeDisposable += pagingAdapter.retryClicks.throttleFirstShort().subscribe {
            viewModel.searchGenreMovies(args.genre)
        }

        compositeDisposable += binding.moviesRecyclerView.onScrolledBottom()
                .throttleFirstShort() // avoid duplicate API calls, 21note
                .subscribe {
                    // don't load next page if it's in requesting, or error, or already on the last page. 21note
                    if (pagingAdapter.isIdleAndNotLastPage) {
                        viewModel.searchGenreMovies(args.genre)
                    }
                }
    }

    override fun renderUI() = with(viewModel) {
        observeOnView(genreMovies) {
            moviesAdapter.submitList(it.data?.results)
            pagingAdapter.pagingState = MapperUI.toPagingState(it)
        }
    }
}
