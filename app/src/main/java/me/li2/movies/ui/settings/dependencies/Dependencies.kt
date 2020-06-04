/*
 * Created by Weiyi Li on 2020-06-04.
 * https://github.com/li2
 */
package me.li2.movies.ui.settings.dependencies

object Dependencies {
    fun getAllDependencies(): List<String> {
        return listOf(
                // frameworks
                "https://developer.android.com/jetpack/androidx",
                "https://developer.android.com/kotlin/ktx",
                "https://github.com/Kotlin/kotlinx.coroutines",
                "https://github.com/Kodein-Framework/Kodein-DI",
                "https://github.com/material-components/material-components-android",

                // network
                "https://square.github.io/retrofit/",
                "https://github.com/square/okhttp",
                "https://github.com/square/moshi",
                "https://firebase.google.com/docs/cloud-messaging",

                // rx
                "https://github.com/ReactiveX/RxAndroid",
                "https://github.com/ReactiveX/RxKotlin",
                "https://github.com/JakeWharton/RxBinding",

                // debug
                "https://firebase.google.com/docs/crashlytics",
                "https://github.com/JakeWharton/timber",
                "https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor",
                "https://github.com/jgilfelt/chuck",
                "https://github.com/amitshekhariitbhu/Android-Debug-Database",

                // 3rd view libs
                "https://bumptech.github.io/glide",
                "https://github.com/romandanylyk/PageIndicatorView",
                "http://facebook.github.io/shimmer-android",
                "https://github.com/chrisbanes/PhotoView",
                "https://github.com/Jay-Goo/RangeSeekBar",
                "https://jitpack.io/p/omegaes/android-link-preview"
        )
    }
}