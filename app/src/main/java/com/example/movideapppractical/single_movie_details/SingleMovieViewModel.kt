package com.example.movideapppractical.single_movie_details

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movideapppractical.db.CartDetails
import com.example.movideapppractical.db.MovieLocalDBRepo
import com.example.movideapppractical.model.MovieDetails
import com.example.movideapppractical.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class SingleMovieViewModel(
    private val movieRepository : MovieDetailesRepository,
    movieId:Int,
    private val repository: MovieLocalDBRepo): ViewModel(){

    private val compositeDisposable =CompositeDisposable()

    private var _updateCart = MutableLiveData<CartDetails>()
    var updateCart: LiveData<CartDetails> = _updateCart

    val movieDetails :LiveData<MovieDetails>by lazy{
        movieRepository.fetchSingleMovieDetails(compositeDisposable,movieId)
    }

    val networkState :LiveData<NetworkState>by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()

    }

    fun insertCartDetail(cartDetails: CartDetails){
        viewModelScope.launch {
            repository.deleteAllData().let {
                repository.insertCartDetails(cartDetails).let {
                    _updateCart.postValue(cartDetails)
                }
            }

        }
    }
}