package me.li2.movies.ui.moviedetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.li2.android.common.arch.Resource
import me.li2.movies.TestCoroutineRule
import me.li2.movies.data.model.MovieItemPagingUI
import me.li2.movies.data.model.MovieItemUI
import me.li2.movies.data.repository.Repository
import me.li2.movies.util.TestCoroutineContextProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MovieDetailViewModel2Test {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var movieItemUI: MovieItemUI

    @Mock
    lateinit var repository: Repository

    @Mock
    lateinit var observer: Observer<Resource<List<MovieItemUI>>>

    @Captor
    private lateinit var argumentCaptor: ArgumentCaptor<Resource<List<MovieItemUI>>>

    private lateinit var viewModel: MovieDetailViewModel2

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = MovieDetailViewModel2(movieItemUI, repository, TestCoroutineContextProvider())
        viewModel.recommendations.observeForever(observer)
    }

    @Test
    fun testSucceed() {
        val movies = listOf(mock(MovieItemUI::class.java))
        val apiResponse = MovieItemPagingUI(movies.toMutableList(), 1, 1, 1)

        testCoroutineRule.runBlockingTest {
            `when`(repository.getMovieRecommendations(anyInt(), anyInt())).thenReturn(apiResponse)

            viewModel.getMovieRecommendations()

            verify(observer, times(2))
                .onChanged(argumentCaptor.capture())
            val values = argumentCaptor.allValues

            assertEquals(2, values.size)
            assertEquals(Resource.loading<List<MovieItemUI>>(), values[0])
            assertEquals(Resource.success(movies), values[1])

//            verify(observer).onChanged(Resource.loading())
//            verify(observer).onChanged(Resource.success(movies))
        }
    }

    @Test
    fun testError() {
        testCoroutineRule.runBlockingTest {
            val exception = RuntimeException("no auth")
            `when`(repository.getMovieRecommendations(anyInt(), anyInt())).thenThrow(exception)

            viewModel.getMovieRecommendations()

            verify(observer, times(2))
                .onChanged(argumentCaptor.capture())
            val values = argumentCaptor.allValues

            assertEquals(2, values.size)
            assertEquals(Resource.loading<List<MovieItemUI>>(), values[0])
            assertEquals(Resource.error<List<MovieItemUI>>(exception), values[1])

//            verify(observer).onChanged(Resource.error(exception))
//            verify(observer).onChanged(Resource.loading())
        }
    }
}