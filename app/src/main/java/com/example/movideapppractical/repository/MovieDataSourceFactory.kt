package com.example.movideapppractical.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movideapppractical.api.MovieDBInterface
import com.example.movideapppractical.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService : MovieDBInterface, private val compositeDisposable: CompositeDisposable,private val sortBy: String)
    : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource =  MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable,sortBy)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}