package me.li2.movies.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import me.li2.movies.util.Constants
import org.jetbrains.anko.defaultSharedPreferences
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideConstants(@ApplicationContext context: Context) = Constants(context)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context) = context.defaultSharedPreferences
}