package me.li2.movies.arch

/**
 * https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/vo/Status.kt
 */
class Resource<out T> private constructor(val status: Status,
                                          val data: T?,
                                          val exception: AppException?) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(exception: AppException?): Resource<T> {
            return Resource(Status.ERROR, null, exception)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}
