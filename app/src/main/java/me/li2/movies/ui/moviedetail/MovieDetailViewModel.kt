package me.li2.movies.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.li2.android.common.arch.*
import me.li2.movies.base.BaseViewModel
import me.li2.movies.data.model.*
import me.li2.movies.util.distinctUntilChanged
import me.li2.movies.util.io

class MovieDetailViewModel : BaseViewModel() {

    private val _movieDetail = MutableLiveData<Resource<MovieDetailUI>>()
    internal val movieDetail: LiveData<Resource<MovieDetailUI>>
        get() = _movieDetail.distinctUntilChanged()

    private val _movieReviews = MutableLiveData<Resource<MovieReviewListUI>>()
    internal val movieReviews: LiveData<Resource<MovieReviewListUI>>
        get() = _movieReviews.distinctUntilChanged()

    private val _youtubeUrl = MutableLiveData<Resource<String?>>()
    internal val youtubeUrl: LiveData<Resource<String?>>
        get() = _youtubeUrl.distinctUntilChanged()

    private val _recommendations = MutableLiveData<Resource<List<MovieItemUI>>>()
    internal val recommendations: LiveData<Resource<List<MovieItemUI>>>
        get() = _recommendations.distinctUntilChanged()

    private val _genreMovies = MutableLiveData<Resource<MovieItemPagingUI>>()
    internal val genreMovies: LiveData<Resource<MovieItemPagingUI>>
        get() = _genreMovies.distinctUntilChanged()

    internal val canLoadMoreGenreMovies: Boolean
        get() = _genreMovies.isIdle() && !_genreMovies.isLastPage()

    fun getMovieDetailScreenData(movieId: Int) {
        getMovieDetail(movieId)
        getMovieReviews(movieId)
        getYouTubeUrl(movieId)
        getMovieRecommendations(movieId)
    }

    private fun getMovieDetail(movieId: Int) {
        _movieDetail.postLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.localDataSource.getMovieDetail(movieId)
                        ?: MapperUI.toMovieDetailUI(repository.getMovieDetail(movieId)).also {
                            repository.localDataSource.saveMovieDetail(it)
                        }
                _movieDetail.postSuccess(result)
//                val movieDetailAPI = repository.getMovieDetail(movieId)
//                val movieDetailUI = MapperUI.toMovieDetailUI(movieDetailAPI)
//                _movieDetail.postSuccess(movieDetailUI)
            } catch (exception: Exception) {
                _movieDetail.postError(exception)
            }
        }
    }

    private fun getMovieReviews(movieId: Int, page: Int = 1) {
        _movieReviews.postLoading()
        io({
            _movieReviews.postError(it)
        }, {
            val movieReviewsAPI = repository.getMovieReviews(movieId, page)
            val movieReviewsUI = MapperUI.toMovieReviewsUI(movieReviewsAPI)
            _movieReviews.postSuccess(movieReviewsUI)
        })
    }

    private fun getYouTubeUrl(movieId: Int) {
        io {
            val movieVideosAPI = repository.getMovieVideos(movieId)
            _youtubeUrl.postSuccess(MapperUI.toYoutubeUrl(movieVideosAPI))
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