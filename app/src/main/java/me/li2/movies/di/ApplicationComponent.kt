package me.li2.movies.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import me.li2.movies.App
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidModule::class,
        AndroidSupportInjectionModule::class,
        HomeActivityModule::class,
        AppModule::class,
        NetworkModule::class,
        RoomModule::class,
    ],
)
interface ApplicationComponent : AndroidInjector<App>