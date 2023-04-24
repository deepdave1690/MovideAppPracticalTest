package com.example.movideapppractical.api

import com.example.movideapppractical.model.MovieDetails
import com.example.movideapppractical.model.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBInterface {

    //https://api.themoviedb.org/3/movie/550?api_key=95f7e72b87c85e8723c1e3910aebaf39

    /* eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5NWY3ZTcyYjg3Yzg1ZTg3MjNjMWUzOTEwYWViYWYzOSIsInN1YiI6IjY0NDRiZWZjMDU4MjI0MDUxMzMzMTQ4NiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.rzX8MR3QJMISxcGr8F-6reiTW6Iy_-9T8NivsOlmzqU*/

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id")id:Int) : Single<MovieDetails>

    @GET("discover/movie")
    fun getPopularMovie(@Query("page")page:Int,@Query("sort_by") sort_by:String) :Single<MovieResponse>

}