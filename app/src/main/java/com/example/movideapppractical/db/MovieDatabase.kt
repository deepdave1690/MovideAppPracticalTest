package com.example.movideapppractical.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope


/**
 * [MovieDatabase] database for the application including a table for [MovieEntry]
 * with the DAO [MovieDao]
 */
// List of the entry classes and associated TypeConverters
@Database(entities = [CartDetails::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    // The associated DAOs for the database
    abstract fun movieDao(): MovieDao?

    companion object {
        private val TAG = MovieDatabase::class.java.simpleName

        // For Singleton instantiation
        private val LOCK = Any()
        private var sInstance: MovieDatabase? = null
        fun getInstance(context: Context): MovieDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    Log.d(
                        TAG,
                        "Creating new database instance"
                    )
                    sInstance = databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java, "MOVIE_DATA"
                    )
                        .build()
                }
            }
            Log.d(TAG, "Getting the database instance")
            return sInstance
        }
    }
}
