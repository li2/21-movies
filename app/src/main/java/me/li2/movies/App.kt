/*
 * Created by Weiyi Li on 2019-09-28.
 * https://github.com/li2
 */
package me.li2.movies

import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import me.li2.android.view.theme.ThemeHelper
import me.li2.movies.AppBuildConfig.configCrashlytics
import me.li2.movies.AppBuildConfig.configDebugLog
import me.li2.movies.data.repository.AppSettings
import me.li2.movies.di.AndroidModule
import me.li2.movies.di.DaggerApplicationComponent
import me.li2.movies.fcm.NotificationUtil.createNotificationChannels
import me.li2.movies.util.Constants
import javax.inject.Inject

class App : DaggerApplication() {

    @Inject
    lateinit var constants: Constants

    @Inject
    lateinit var appSettings: AppSettings


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder()
            .androidModule(AndroidModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        configDebugLog(this)
        configCrashlytics()
        constants.init()
        createNotificationChannels(this)
        AndroidThreeTen.init(this)
        ThemeHelper.applyTheme(appSettings.themePref)
    }
}
