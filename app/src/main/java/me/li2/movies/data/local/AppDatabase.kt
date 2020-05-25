/*
 * Created by Weiyi Li on 2020-05-04.
 * https://github.com/li2
 */
package me.li2.movies.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.li2.movies.AppBuildConfig.configDebugDB
import me.li2.movies.data.local.AppDatabase.Companion.DATABASE_VERSION
import me.li2.movies.data.local.converters.GenreListConverter
import me.li2.movies.data.local.converters.LocalDateConverter
import me.li2.movies.data.local.converters.TrailerListConverter
import me.li2.movies.data.model.MovieDetailUI
import me.li2.movies.data.model.MovieReviewUI
import me.li2.movies.data.model.Trailer

@Database(entities = [
    MovieDetailUI::class,
    MovieReviewUI::class,
    Trailer::class
], version = DATABASE_VERSION, exportSchema = false)
@TypeConverters(
        GenreListConverter::class,
        LocalDateConverter::class,
        TrailerListConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun reviewsDao(): ReviewDao
    abstract fun trailerDao(): TrailerDao

    companion object {
        internal const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "21CinemasApp.db"
        const val TABLE_MOVIES = "MoviesTable"
        const val TABLE_REVIEWS = "ReviewsTable"
        const val TABLE_TRAILERS = "TrailersTable"
        private const val SELECT_FROM = "SELECT * FROM"
        private const val DELETE_FROM = "DELETE FROM"
        internal const val SELECT_FROM_MOVIES = "$SELECT_FROM $TABLE_MOVIES"
        internal const val SELECT_FROM_REVIEWS = "$SELECT_FROM $TABLE_REVIEWS"
        internal const val DELETE_FROM_REVIEWS = "$DELETE_FROM $TABLE_REVIEWS"
        internal const val SELECT_FROM_TRAILERS = "$SELECT_FROM $TABLE_TRAILERS"

        /*it's MANDATORY to write test for migrations https://developer.android.com/training/data-storage/room/migrating-db-versions.html
        cause it can cause a crash loop
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)*/
        fun buildRoomDatabase(context: Context): AppDatabase = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build().also {
                    configDebugDB()
                }
    }
}