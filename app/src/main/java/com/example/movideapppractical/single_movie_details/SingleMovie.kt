package com.example.movideapppractical.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.movideapppractical.R
import com.example.movideapppractical.api.MovieDBClient
import com.example.movideapppractical.api.MovieDBInterface
import com.example.movideapppractical.api.POSTER_BASE_URL
import com.example.movideapppractical.db.CartDetails
import com.example.movideapppractical.db.MovieDatabase
import com.example.movideapppractical.db.MovieLocalDBRepo
import com.example.movideapppractical.model.MovieDetails
import com.example.movideapppractical.repository.NetworkState
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailesRepository
    private lateinit var repository: MovieLocalDBRepo
    private var totalTicket = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId:Int=intent.getIntExtra("id",1)
        totalTicket = intent.getIntExtra("total_ticket",1)

        val apiService: MovieDBInterface = MovieDBClient.getClient()
        movieRepository= MovieDetailesRepository(apiService)
        repository = MovieLocalDBRepo(MovieDatabase.getInstance(applicationContext)!!.movieDao()!!)

        viewModel=getViewModel(movieId)
        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.updateCart.observe(this){
            Toast.makeText(applicationContext,"Your Movie ticket successfully booked",Toast.LENGTH_LONG).show()
        }

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility=if(it== NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility=if(it== NetworkState.ERROR) View.VISIBLE else View.GONE
        })

        icon_increase_value.setOnClickListener {
            var qty = text_quantity.text.toString().toInt()
            qty += 1
            text_quantity.text = qty.toString()
        }
        icon_decrease_value.setOnClickListener {
            var qty = text_quantity.text.toString().toInt()
            if (qty > 1) {
                qty = (qty - 1)
                text_quantity.text = qty.toString()
            }
        }
        addToCart.setOnClickListener{
            val cartDetails = CartDetails(movieId.toString(),text_quantity.text.toString().toInt())
            viewModel.insertCartDetail(cartDetails)
        }

    }

    fun bindUI( it: MovieDetails){
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString() + " minutes"
        movie_overview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster);

        text_quantity.text = totalTicket.toString()


    }
    private fun getViewModel(movieId:Int): SingleMovieViewModel {
        return ViewModelProviders.of(this,MyViewModelFactory(movieRepository,movieId,repository) )[SingleMovieViewModel::class.java]
    }

    class MyViewModelFactory(private val movieDetailesRepository: MovieDetailesRepository,
                             private val movieId: Int,
                                private val repository: MovieLocalDBRepo): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SingleMovieViewModel::class.java!!)) {
                SingleMovieViewModel(this.movieDetailesRepository,this.movieId,this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}