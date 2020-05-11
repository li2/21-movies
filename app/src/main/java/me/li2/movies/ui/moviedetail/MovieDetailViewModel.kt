package me.li2.movies.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.li2.android.common.arch.*
import me.li2.movies.base.BaseViewModel
import me.li2.movies.data.model.*
import me.li2.movies.util.*

class MovieDetailViewModel : BaseViewModel() {

    private val _movieDetail = MutableLiveData<Resource<MovieDetailUI>>()
    internal val movieDetail: LiveData<Resource<MovieDetailUI>>
        get() = _movieDetail.distinctUntilChanged()

    private val _movieReviews = MutableLiveData<Resource<List<MovieReviewUI>>>()
    internal val movieReviews: LiveData<Resource<List<MovieReviewUI>>>
        get() = _movieReviews.distinctUntilChanged()

    private val _movieTrailer = MutableLiveData<Resource<Trailer?>>()
    internal val movieTrailer: LiveData<Resource<Trailer?>>
        get() = _movieTrailer.distinctUntilChanged()

    private val _recommendations = MutableLiveData<Resource<List<MovieItemUI>>>()
    internal val recommendations: LiveData<Resource<List<MovieItemUI>>>
        get() = _recommendations.distinctUntilChanged()

    var movieItem: MovieItemUI? = null

    val movieDetailRows = CombinedLiveData<List<BaseRowData?>>(movieDetail, movieTrailer, movieReviews, recommendations) { results ->
        val movieDetail = results[0]?.checkedResourceItem<MovieDetailUI>()
                ?: movieItem?.let { MapperUI.toMovieDetailUI(it) }
        val trailer = results[1]?.checkedResourceItem<Trailer>()
        val reviews = results[2]?.checkedResourceListItem<MovieReviewUI>()
        val recommendations = results[3]?.checkedResourceListItem<MovieItemUI>()

        return@CombinedLiveData listOf(
                DetailRowData(movieDetail = movieDetail?.copy(youtubeTrailerUrl = trailer?.url)),
                ReviewsRowData(reviews = reviews),
                RecMoviesRowData(movies = recommendations))
    }

    private val _genreMovies = MutableLiveData<Resource<MovieItemPagingUI>>()
    internal val genreMovies: LiveData<Resource<MovieItemPagingUI>>
        get() = _genreMovies.distinctUntilChanged()

    internal val canLoadMoreGenreMovies: Boolean
        get() = _genreMovies.isIdle() && !_genreMovies.isLastPage()

    fun getMovieDetailScreenData(movieId: Int) {
        getMovieDetail(movieId)
        getMovieReviews(movieId)
        getMovieTrailer(movieId)
        getMovieRecommendations(movieId)
    }

    private fun getMovieDetail(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMovieDetail(movieId, _movieDetail)
        }
    }

    private fun getMovieReviews(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMovieReviews(movieId, _movieReviews)
        }
    }

    private fun getMovieTrailer(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMovieTrailer(movieId, _movieTrailer)
        }
    }

    private fun getMovieRecommendations(movieId: Int, page: Int = 1) {
        _recommendations.postLoading()
        io({
            _recommendations.postError(it)
        }, {
            val movies = repository.getMovieRecommendations(movieId, page).results
                    .take(10)
                    .map { MapperUI.toMovieItemUI(it) }
            _recommendations.postSuccess(movies)
        })
    }

    fun searchGenreMovies(genre: String) {
        if (_genreMovies.isLoading()) {
            return
        }
        _genreMovies.postLoading()
        io({
            _genreMovies.postError(it)
        }, {
            val api = repository.searchMovies(genre, _genreMovies.nextPage())
            val ui = MapperUI.toMovieItemPagingUI(api)
            _genreMovies.postSuccess(ui.copy(results = _genreMovies.appendResults(ui.results)))
        })
    }
}