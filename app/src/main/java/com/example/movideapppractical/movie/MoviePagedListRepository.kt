package com.example.movideapppractical.movie
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.LoadStateAdapter
import androidx.paging.PagedList
import com.example.movideapppractical.api.MovieDBInterface
import com.example.movideapppractical.api.POST_PER_PAGE
import com.example.movideapppractical.model.Movie
import com.example.movideapppractical.repository.MovieDataSource
import com.example.movideapppractical.repository.MovieDataSourceFactory
import com.example.movideapppractical.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository (private val apiService : MovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable, sortBy: String) : LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable,sortBy)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        if(this::moviesDataSourceFactory.isInitialized){
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState)
        }else{
                return MutableLiveData<NetworkState>()
        }
    }

}