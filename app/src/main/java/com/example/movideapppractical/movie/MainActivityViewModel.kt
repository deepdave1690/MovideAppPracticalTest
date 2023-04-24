package com.example.movideapppractical.movie
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.movideapppractical.db.CartDetails
import com.example.movideapppractical.db.MovieLocalDBRepo
import com.example.movideapppractical.model.Movie
import com.example.movideapppractical.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class MainActivityViewModel(private val movieRepository : MoviePagedListRepository,
                            private val repository: MovieLocalDBRepo
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _updateCart = MutableLiveData<CartDetails>()
    var updateCart: LiveData<CartDetails> = _updateCart

    private val movieInput = MutableLiveData<String>()

    val moviePagedList : LiveData<PagedList<Movie>> =  Transformations.switchMap(movieInput){
        movieRepository.fetchLiveMoviePagedList(compositeDisposable,it)
    }

    val  moviePagedListSortBy : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable,"")
    }

    fun setSortBy(sortBy : String){
        movieInput.value = sortBy
    }

    val  networkState : LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        moviePagedList.value
        return moviePagedList.value?.isEmpty() ?: true
    }

    fun getCartDetails(){
        viewModelScope.launch {
            repository.getAllCartData().let {
                if(it.size > 0) {
                    _updateCart.postValue(it.get(0))
                    for (i in 0..it.size - 1) {
                        Log.e(
                            "Deep $i",
                            " Cart ID :" + it.get(i).movieId + " Movide Ticket Coiunt" + it.get(i).totalTickets
                        )
                    }
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}