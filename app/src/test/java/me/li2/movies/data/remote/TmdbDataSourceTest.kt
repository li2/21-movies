package me.li2.movies.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

//failed due to lateinit property context has not been initialized

@RunWith(JUnit4::class)
class TmdbDataSourceTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var remote: TmdbDataSource

    @Before
    fun setup() {
        remote = TmdbDataSource()
    }

    @Test
    fun checkAPIResponseForGetGenres() {
        runBlocking {
            val genres = remote.getGenres().genres
            assertThat(genres.size, CoreMatchers.`is`(CoreMatchers.not(0)))
            genres.forEach {
                assertNotNull(it.id)
                assert(it.name.isNotBlank())
            }
        }
    }
}