package me.li2.movies.ui.moviedetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.runBlocking
import me.li2.android.common.arch.Resource
import me.li2.movies.data.model.MovieItemPagingUI
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.data.repository.Repository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MovieDetailViewModel2Test {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var movieItemUI: MovieItemUI

    @Mock
    lateinit var repository: Repository

    @Mock
    lateinit var observer: Observer<Resource<List<MovieItemUI>>>

    private lateinit var viewModel: MovieDetailViewModel2

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = MovieDetailViewModel2(movieItemUI, repository)
        viewModel.recommendations.observeForever(observer)
    }

    @Test
    fun testSucceed() {
        val movies = listOf(mock(MovieItemUI::class.java))
        val apiResponse = MovieItemPagingUI(movies.toMutableList(), 1, 1, 1)

        runBlocking {
            `when`(repository.getMovieRecommendations(anyInt(), anyInt())).thenReturn(apiResponse)

            viewModel.getMovieRecommendations()

            verify(observer).onChanged(Resource.loading(emptyList()))
            verify(observer).onChanged(Resource.success(movies))
        }
    }

    @Test
    fun testError() {
        runBlocking {
            val exception = RuntimeException("no auth")
            `when`(repository.getMovieRecommendations(anyInt(), anyInt())).thenThrow(exception)

            viewModel.getMovieRecommendations()

            verify(observer).onChanged(Resource.loading(emptyList()))
            verify(observer).onChanged(Resource.error(exception))
        }
    }
}