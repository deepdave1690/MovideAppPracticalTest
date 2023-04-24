package com.example.movideapppractical.single_movie_details

import androidx.lifecycle.LiveData
import com.example.movideapppractical.api.MovieDBInterface
import com.example.movideapppractical.model.MovieDetails
import com.example.movideapppractical.repository.MovieDetailsNetworkDataSource
import com.example.movideapppractical.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailesRepository(private val apiService: MovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource
    fun fetchSingleMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieDetails> {

        movieDetailsNetworkDataSource =
            MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse
    }

    fun getMovieDetailsNetworkState():LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }

}