package me.li2.movies

import com.google.firebase.crashlytics.FirebaseCrashlytics
import me.li2.movies.util.doNothing

object AppBuildConfig {
    const val DEBUG = false

    fun configDebugLog(app: App) {
        doNothing()
    }

    fun configCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }

    fun configDebugDB() {
        doNothing()
    }
}
