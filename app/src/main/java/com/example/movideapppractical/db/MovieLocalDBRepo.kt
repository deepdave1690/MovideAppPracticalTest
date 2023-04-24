package com.example.movideapppractical.db

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Dao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MovieLocalDBRepo(private val movieDao: MovieDao, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {


    suspend fun getAllCartData(): MutableList<CartDetails> = withContext(ioDispatcher)  {
        return@withContext movieDao.getCartDetails()
    }

    suspend fun insertCartDetails(cartDetails: CartDetails)= withContext(ioDispatcher)  {
        movieDao.saveCartDetails(cartDetails)
    }

    suspend fun deleteAllData() = withContext(ioDispatcher)  {
        movieDao.deleteCartDetails()
    }
}