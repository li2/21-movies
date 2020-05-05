package me.li2.movies

import android.os.Build
import com.amitshekhar.DebugDB
import com.facebook.stetho.Stetho
import timber.log.Timber

fun App.configDebugBuild() {
    setupStetho(this)
    setupTimber()
}

private fun setupStetho(app: App) {
    if ("robolectric" != Build.FINGERPRINT) {
        Stetho.initializeWithDefaults(app)
    }
}

private fun setupTimber() {
    Timber.plant(object : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String? {
            return "[${super.createStackElementTag(element)}#${element.methodName}:${element.lineNumber}]"
        }
    })
}

fun setupDebugDB() {
    DebugDB.getAddressLog()
}