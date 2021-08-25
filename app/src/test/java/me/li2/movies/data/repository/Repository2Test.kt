package me.li2.movies.data.repository

import kotlinx.coroutines.runBlocking
import me.li2.movies.data.model.Genre
import me.li2.movies.data.model.GenresAPI
import me.li2.movies.data.remote.TmdbDataSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class Repository2Test {

    @Mock
    lateinit var tmdbDataSource: TmdbDataSource

    private lateinit var repository: Repository2

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = Repository2(tmdbDataSource)
    }

    @Test
    fun test_getGenres_when_network_is_available_then_genres_is_returned() {
        val genres = listOf(
            Genre(1, "Action"),
            Genre(2, "Comedy")
        )

        runBlocking {
            `when`(tmdbDataSource.getGenres()).thenReturn(GenresAPI(genres))

            val genresUI = repository.getGenresAsync()

            assertEquals(genresUI.size, genres.size)

            genresUI.forEachIndexed { index, genreUI ->
                assertEquals(genreUI.id, genres[index].id)
                assertEquals(genreUI.name, genres[index].name)
            }
        }
    }

    @Test
    fun test_getGenres_when_network_is_unavailable_then_error_is_returned() {
        runBlocking {
            `when`(tmdbDataSource.getGenres()).thenThrow(RuntimeException("no network"))

            val exception = assertThrows(RuntimeException::class.java) {
                runBlocking {
                    repository.getGenresAsync()
                }
            }
            assertEquals("no network", exception.message)
        }
    }
}