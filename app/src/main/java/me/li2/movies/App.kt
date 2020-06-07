/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies

import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import me.li2.movies.AppBuildConfig.configCrashlytics
import me.li2.movies.AppBuildConfig.configDebugLog
import me.li2.movies.data.repository.AppSettings
import me.li2.movies.di.MainComponent.appModule
import me.li2.movies.di.networkModule
import me.li2.movies.fcm.NotificationUtil.createNotificationChannels
import me.li2.movies.util.Constants
import me.li2.movies.util.ThemeHelper
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.instance

class App : MultiDexApplication(), KodeinAware {
    override val kodein = ConfigurableKodein(mutable = true)
    private val constants by instance<Constants>()
    private val appSettings by instance<AppSettings>()
    private var overrideModule: Kodein.Module? = null

    override fun onCreate() {
        super.onCreate()
        context = this
        configDebugLog(this)
        configCrashlytics()
        setupKodein()
        constants.init()
        createNotificationChannels(this)
        AndroidThreeTen.init(this)
        ThemeHelper.applyTheme(appSettings.themePref)
    }

    private fun setupKodein() {
        kodein.apply {
            clear()
            addImport(androidXModule(this@App))
            addImport(networkModule)
            addImport(appModule, allowOverride = true)
            overrideModule?.let {
                addImport(it, true)
            }
        }
    }

    companion object {
        lateinit var context: App
    }
}
