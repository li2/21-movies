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

    fun getMovieDetail(movieId: Int) {
        movieDetailMutableLiveData.postLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieDetailAPI = repository.getMovieDetailAsync(movieId)
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
            val movieReviewsAPI = repository.getMovieReviewsAsync(movieId, page)
            val movieReviewsUI = MapperUI.toMovieReviewsUI(movieReviewsAPI)
            movieReviewsMutableLiveData.postSuccess(movieReviewsUI)
        })
    }

    fun getYouTubeUrl(movieId: Int) {
        ioWithLiveData(youtubeUrlMutableLiveData) {
            val movieVideosAPI = repository.getMovieVideosAsync(movieId)
            MapperUI.toYoutubeUrl(movieVideosAPI)
        }
    }
}

