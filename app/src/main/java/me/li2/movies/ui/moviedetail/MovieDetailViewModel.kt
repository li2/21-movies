package me.li2.movies.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.postError
import me.li2.android.common.arch.postLoading
import me.li2.android.common.arch.postSuccess
import me.li2.movies.base.BaseViewModel
import me.li2.movies.data.model.*
import me.li2.movies.data.remote.TmdbApi
import me.li2.movies.util.distinctUntilChanged
import me.li2.movies.util.io
import me.li2.movies.util.isRequestInProgress

class MovieDetailViewModel : BaseViewModel() {

    private val movieDetailMutableLiveData: MutableLiveData<Resource<MovieDetailUI>> = MutableLiveData()
    internal val movieDetailLiveData: LiveData<Resource<MovieDetailUI>> = movieDetailMutableLiveData

    private val movieReviewsMutableLiveData: MutableLiveData<Resource<MovieReviewListUI>> = MutableLiveData()
    internal val movieReviewsLiveData: LiveData<Resource<MovieReviewListUI>> = movieReviewsMutableLiveData

    private val youtubeUrlMutableLiveData: MutableLiveData<Resource<String?>> = MutableLiveData()
    internal val youtubeUrlLiveData: LiveData<Resource<String?>> = youtubeUrlMutableLiveData

    private val recommendationsMutableLiveData: MutableLiveData<Resource<List<MovieItemUI>>> = MutableLiveData()
    internal val recommendationsLiveData: LiveData<Resource<List<MovieItemUI>>> = recommendationsMutableLiveData

    private val _genreMovies = MutableLiveData<Resource<MovieItemPagedUI>>()
    internal val genreMovies: LiveData<Resource<MovieItemPagedUI>>
        get() = _genreMovies.distinctUntilChanged()

    fun getMovieDetailScreenData(movieId: Int) {
        getMovieDetail(movieId)
        getMovieReviews(movieId)
        getYouTubeUrl(movieId)
        getMovieRecommendations(movieId)
    }

    private fun getMovieDetail(movieId: Int) {
        movieDetailMutableLiveData.postLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieDetailAPI = repository.getMovieDetail(movieId)
                val movieDetailUI = MapperUI.toMovieDetailUI(movieDetailAPI)
                movieDetailMutableLiveData.postSuccess(movieDetailUI)
            } catch (exception: Exception) {
                movieDetailMutableLiveData.postError(exception)
            }
        }
    }

    private fun getMovieReviews(movieId: Int, page: Int = 1) {
        movieReviewsMutableLiveData.postLoading()
        io({
            movieReviewsMutableLiveData.postError(it)
        }, {
            val movieReviewsAPI = repository.getMovieReviews(movieId, page)
            val movieReviewsUI = MapperUI.toMovieReviewsUI(movieReviewsAPI)
            movieReviewsMutableLiveData.postSuccess(movieReviewsUI)
        })
    }

    private fun getYouTubeUrl(movieId: Int) {
        io {
            val movieVideosAPI = repository.getMovieVideos(movieId)
            youtubeUrlMutableLiveData.postSuccess(MapperUI.toYoutubeUrl(movieVideosAPI))
        }
    }

    private fun getMovieRecommendations(movieId: Int, page: Int = 1) {
        recommendationsMutableLiveData.postLoading()
        io({
            recommendationsMutableLiveData.postError(it)
        }, {
            val movies = repository.getMovieRecommendations(movieId, page).results
                    .take(10)
                    .map { MapperUI.toMovieItemUI(it) }
            recommendationsMutableLiveData.postSuccess(movies)
        })
    }

    fun searchGenreMovies(genre: String) {
        if (_genreMovies.isRequestInProgress()) {
            return
        }
        _genreMovies.postLoading()
        io({
            _genreMovies.postError(it)
        }, {
            val nextPage = _genreMovies.value?.data?.let { it.page + 1 }
                    ?: TmdbApi.TMDB_STARTING_PAGE_INDEX
            val api = repository.searchMovies(genre, nextPage)
            val ui = MapperUI.toMovieItemPagedUI(api)
            // append results
            val appendedResults = _genreMovies.value?.data?.results.orEmpty().toMutableList()
            appendedResults.addAll(ui.results)
            _genreMovies.postSuccess(ui.copy(results = appendedResults))
        })
    }
}