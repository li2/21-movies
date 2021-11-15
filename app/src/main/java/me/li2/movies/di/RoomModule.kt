package me.li2.movies.di

import android.content.Context
import dagger.Module
import dagger.Provides
import me.li2.movies.data.local.AppDatabase
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.buildRoomDatabase(context)
    }
}