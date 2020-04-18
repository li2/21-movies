package me.li2.movies.data.remote

import kotlinx.coroutines.Deferred
import me.li2.movies.data.model.TmdbMoviesListAPI
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/upcoming")
    fun getUpcomingMoviesAsync(@Query("page") page: Int): Deferred<TmdbMoviesListAPI>


    companion object {
        /**
         * @see <a href="https://developers.themoviedb.org/3/getting-started/images">Images</a>
         */
        fun imageUrl(path: String) = "https://image.tmdb.org/t/p/w500/$path"
    }
}