package me.li2.movies.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.li2.movies.AppBuildConfig.configDebugDB
import me.li2.movies.data.local.AppDatabase.Companion.DATABASE_VERSION
import me.li2.movies.data.local.converters.GenreListConverter
import me.li2.movies.data.model.MovieDetailUI

@Database(entities = [
    MovieDetailUI::class
], version = DATABASE_VERSION, exportSchema = true)
@TypeConverters(GenreListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        internal const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "21CinemasApp.db"
        const val TABLE_MOVIE_DETAIL = "Table_Movie_detail"
        private const val SELECT_FROM = "SELECT * FROM"
        internal const val SELECT_FROM_MOVIE_DETAIL = "$SELECT_FROM $TABLE_MOVIE_DETAIL"

        /*it's MANDATORY to write test for migrations https://developer.android.com/training/data-storage/room/migrating-db-versions.html
        cause it can cause a crash loop
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)*/
        fun buildRoomDatabase(context: Context): AppDatabase = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME)
                .build().also {
                    configDebugDB()
                }
    }
}