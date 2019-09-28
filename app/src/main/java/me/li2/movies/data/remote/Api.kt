package me.li2.movies.data.remote

import com.serjltt.moshi.adapters.Wrapped
import kotlinx.coroutines.Deferred
import me.li2.movies.data.model.MovieAPI
import retrofit2.http.GET
import retrofit2.http.Query

@JvmSuppressWildcards
interface Api {
    @Wrapped(path = [DATA_WRAPPER])
    @GET("movies")
    fun getMoviesAsync(@Query("type") type: String): Deferred<List<MovieAPI>>

    companion object {
        private const val DATA_WRAPPER = "data"
    }
}
