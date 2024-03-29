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
import me.li2.movies.data.model.*

@Database(entities = [
    MovieDetailUI::class,
    MovieReviewUI::class,
    Trailer::class,
    GenreUI::class,
    WatchlistMovieDB::class
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
    abstract fun genresDao(): GenresDao
    abstract fun watchlistDao(): WatchlistDao

    companion object {
        internal const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "21CinemasApp.db"
        const val TABLE_MOVIES = "MoviesTable"
        const val TABLE_REVIEWS = "ReviewsTable"
        const val TABLE_TRAILERS = "TrailersTable"
        const val TABLE_GENRES = "GenresTable"
        const val TABLE_WATCHLIST = "WatchlistTable"
        private const val SELECT_FROM = "SELECT * FROM"
        private const val DELETE_FROM = "DELETE FROM"
        internal const val SELECT_FROM_MOVIES = "$SELECT_FROM $TABLE_MOVIES"
        internal const val SELECT_FROM_REVIEWS = "$SELECT_FROM $TABLE_REVIEWS"
        internal const val DELETE_FROM_REVIEWS = "$DELETE_FROM $TABLE_REVIEWS"
        internal const val SELECT_FROM_TRAILERS = "$SELECT_FROM $TABLE_TRAILERS"
        internal const val SELECT_FROM_GENRES = "$SELECT_FROM $TABLE_GENRES"
        internal const val SELECT_FROM_WATCHLIST = "$SELECT_FROM $TABLE_WATCHLIST"
        internal const val DELETE_FROM_WATCHLIST = "$DELETE_FROM $TABLE_WATCHLIST"

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