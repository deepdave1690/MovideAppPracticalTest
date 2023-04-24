package com.example.movideapppractical

import android.app.Application
import com.example.movideapppractical.db.MovieDatabase
import com.example.movideapppractical.db.MovieLocalDBRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AppClass : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { MovieDatabase.getInstance(this) }
    val repository by lazy { MovieLocalDBRepo(database!!.movieDao()!!) }
}