/*
 * Created by Weiyi Li on 2020-05-06.
 * https://github.com/li2
 */
package me.li2.movies.data.repository

import androidx.lifecycle.MutableLiveData
import me.li2.android.common.arch.Resource
import me.li2.android.common.arch.postError
import me.li2.android.common.arch.postLoading
import me.li2.android.common.arch.postSuccess

/**
 * A generic class provides a resource backed by both the database and the network.
 *
 * You can read more about it in:
 * @see <a href="https://developer.android.com/jetpack/docs/guide#addendum">exposing network status</a>
 * @see <a href="https://developer.android.com/arch">Architecture Guide</a>
 * @see <a href="https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/repository/NetworkBoundResource.java">NetworkBoundResource.java</a>
 */
abstract class NetworkBoundResource<T>(private val result: MutableLiveData<Resource<T>>) {

    suspend fun load() {
        result.postLoading()
        val dbSource = loadFromDb()
        if (dbSource != null) {
            result.postSuccess(dbSource)
        }
        if (shouldFetch(dbSource)) {
            try {
                val apiSource = fetch()
                result.postSuccess(apiSource)
                saveFetchResult(apiSource)
            } catch (e: Exception) {
                onFetchFailed()
                result.postError(e)
            }
        }
    }

    /**
     * Called to get the cached data from the database.
     */
    protected abstract suspend fun loadFromDb(): T?

    /**
     * Called with the data in the database to decide whether it should be fetched from the network.
     */
    protected abstract fun shouldFetch(data: T?): Boolean

    /**
     * Make a web service API call.
     */
    @Throws(Exception::class)
    protected abstract suspend fun fetch(): T

    /**
     * Called to save the result of the API response into the database
     */
    protected abstract suspend fun saveFetchResult(data: T): Any

    /**
     * Called when fetch fails. The child class may want to reset components like rate limiter.
     */
    protected open fun onFetchFailed() {}
}
