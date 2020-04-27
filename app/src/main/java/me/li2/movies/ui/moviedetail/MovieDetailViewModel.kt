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
import me.li2.movies.data.model.MapperUI
import me.li2.movies.data.model.MovieDetailUI
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.data.model.MovieReviewListUI
import me.li2.movies.util.io
import me.li2.movies.util.ioWithLiveData

class MovieDetailViewModel : BaseViewModel() {

    private val movieDetailMutableLiveData: MutableLiveData<Resource<MovieDetailUI>> = MutableLiveData()
    internal val movieDetailLiveData: LiveData<Resource<MovieDetailUI>> = movieDetailMutableLiveData

    private val movieReviewsMutableLiveData: MutableLiveData<Resource<MovieReviewListUI>> = MutableLiveData()
    internal val movieReviewsLiveData: LiveData<Resource<MovieReviewListUI>> = movieReviewsMutableLiveData

    private val youtubeUrlMutableLiveData: MutableLiveData<Resource<String?>> = MutableLiveData()
    internal val youtubeUrlLiveData: LiveData<Resource<String?>> = youtubeUrlMutableLiveData

    private val recommendationsMutableLiveData: MutableLiveData<Resource<List<MovieItemUI>>> = MutableLiveData()
    internal val recommendationsLiveData: LiveData<Resource<List<MovieItemUI>>> = recommendationsMutableLiveData

    private val genreMoviesMutableLiveData: MutableLiveData<Resource<List<MovieItemUI>>> = MutableLiveData()
    internal val genreMoviesLiveData: LiveData<Resource<List<MovieItemUI>>> = genreMoviesMutableLiveData

    fun getMovieDetail(movieId: Int) {
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

    fun getMovieReviews(movieId: Int, page: Int = 1) {
        movieReviewsMutableLiveData.postLoading()
        io({
            movieReviewsMutableLiveData.postError(it)
        }, {
            val movieReviewsAPI = repository.getMovieReviews(movieId, page)
            val movieReviewsUI = MapperUI.toMovieReviewsUI(movieReviewsAPI)
            movieReviewsMutableLiveData.postSuccess(movieReviewsUI)
        })
    }

    fun getYouTubeUrl(movieId: Int) {
        ioWithLiveData(youtubeUrlMutableLiveData) {
            val movieVideosAPI = repository.getMovieVideos(movieId)
            MapperUI.toYoutubeUrl(movieVideosAPI)
        }
    }

    fun getMovieRecommendations(movieId: Int, page: Int = 1) {
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

    fun searchGenreMovies(genre: String, page: Int = 1) {
        genreMoviesMutableLiveData.postLoading()
        io({
            genreMoviesMutableLiveData.postError(it)
        }, {
            val movies = repository.searchMovies(genre, page).results
                    .take(20)
                    .map { MapperUI.toMovieItemUI(it) }
            genreMoviesMutableLiveData.postSuccess(movies)
        })
    }
}
