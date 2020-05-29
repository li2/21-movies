/*
 * Created by Weiyi Li on 2020-04-25.
 * https://github.com/li2
 */
package me.li2.movies.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.li2.android.common.arch.*
import me.li2.movies.base.BaseViewModel
import me.li2.movies.data.model.*
import me.li2.movies.util.distinctUntilChanged
import me.li2.movies.util.io

class MovieDetailViewModel(movieItem: MovieItemUI) : BaseViewModel() {

    private val _movieDetail = MutableLiveData(Resource.loading(MapperUI.toMovieDetailUI(movieItem)))
    internal val movieDetail: LiveData<Resource<MovieDetailUI>>
        get() = _movieDetail.distinctUntilChanged()

    private val _trailers = MutableLiveData<Resource<List<Trailer>>>(Resource.loading(emptyList()))
    internal val trailers: LiveData<Resource<List<Trailer>>>
        get() = _trailers.distinctUntilChanged()

    private val _credits = MutableLiveData(Resource.loading(CreditListUI(movieId = movieItem.id, casts = emptyList(), crews = emptyList())))
    internal val credits: LiveData<Resource<CreditListUI>>
        get() = _credits.distinctUntilChanged()

    private val _movieReviews = MutableLiveData<Resource<List<MovieReviewUI>>>(Resource.loading(emptyList()))
    internal val movieReviews: LiveData<Resource<List<MovieReviewUI>>>
        get() = _movieReviews.distinctUntilChanged()

    private val _recommendations = MutableLiveData<Resource<List<MovieItemUI>>>(Resource.loading(emptyList()))
    internal val recommendations: LiveData<Resource<List<MovieItemUI>>>
        get() = _recommendations.distinctUntilChanged()

    internal var movieDetailRows: MediatorLiveData<List<BaseRowData>>

    init {
        @Suppress("UNCHECKED_CAST")
        movieDetailRows = combineLatest(movieDetail, trailers, credits, movieReviews, recommendations) { results ->
            val movieDetail = results[0] as Resource<MovieDetailUI>
            val trailers = results[1] as Resource<List<Trailer>>
            val credits = results[2] as Resource<CreditListUI>
            val reviews = results[3] as Resource<List<MovieReviewUI>>
            val recommendations = results[4] as Resource<List<MovieItemUI>>

            listOf(DetailRowData(movieDetail = movieDetail),
                    TrailersRowData(trailers = trailers),
                    CreditsRowData(credits = credits),
                    ReviewsRowData(reviews = reviews),
                    RecMoviesRowData(movies = recommendations))
        }
    }

    fun getMovieDetailScreenData(movieId: Int) {
        getMovieDetail(movieId)
        getMovieCredits(movieId)
        getMovieReviews(movieId)
        getMovieTrailers(movieId)
        getMovieRecommendations(movieId)
    }

    private fun getMovieDetail(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMovieDetail(movieId, _movieDetail)
        }
    }

    private fun getMovieCredits(movieId: Int) {
        _credits.postLoading()
        io({
            _credits.postError(it)
        }, {
            val credits = repository.getMovieCredits(movieId)
            _credits.postSuccess(credits)
        })
    }

    private fun getMovieReviews(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMovieReviews(movieId, _movieReviews)
        }
    }

    private fun getMovieTrailers(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMovieTrailers(movieId, _trailers)
        }
    }

    private fun getMovieRecommendations(movieId: Int, page: Int = 1) {
        _recommendations.postLoading()
        io({
            _recommendations.postError(it)
        }, {
            val movies = repository.getMovieRecommendations(movieId, page).results
                    .map { MapperUI.toMovieItemUI(it) }
            _recommendations.postSuccess(movies)
        })
    }
}