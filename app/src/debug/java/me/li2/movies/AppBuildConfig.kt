package me.li2.movies

import android.os.Build
import com.amitshekhar.DebugDB
import com.facebook.stetho.Stetho
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

object AppBuildConfig {
    const val DEBUG = true

    fun configDebugLog(app: App) {
        // setup Stetho
        if ("robolectric" != Build.FINGERPRINT) {
            Stetho.initializeWithDefaults(app)
        }
        // setup Timber
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return "[${super.createStackElementTag(element)}#${element.methodName}:${element.lineNumber}]"
            }
        })
    }

    fun configCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }

    fun configDebugDB() {
        DebugDB.getAddressLog()
    }
}
