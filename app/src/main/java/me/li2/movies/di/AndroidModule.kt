package me.li2.movies.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AndroidModule(private val application: Application) {

    @Provides
    @ApplicationContext
    fun provideApplicationContext(): Context {
        return application
    }
}