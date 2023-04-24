package com.example.movideapppractical.movie


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movideapppractical.R
import com.example.movideapppractical.api.MovieDBClient
import com.example.movideapppractical.api.MovieDBInterface
import com.example.movideapppractical.db.CartDetails
import com.example.movideapppractical.db.MovieDatabase
import com.example.movideapppractical.db.MovieLocalDBRepo
import com.example.movideapppractical.repository.NetworkState
import com.example.movideapppractical.single_movie_details.SingleMovie
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var textCartItemCount: TextView? = null
    private lateinit var viewModel: MainActivityViewModel
    var mCartItemCount = 0

    lateinit var movieRepository: MoviePagedListRepository
    lateinit var movieAdapter: PopularMoviePagedListAdapter
    private lateinit var repository: MovieLocalDBRepo
    private lateinit var cartDetails: CartDetails

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService: MovieDBInterface = MovieDBClient.getClient()
        repository = MovieLocalDBRepo(MovieDatabase.getInstance(applicationContext)!!.movieDao()!!)

        movieRepository = MoviePagedListRepository(apiService)

        viewModel = getViewModel()

        movieAdapter = PopularMoviePagedListAdapter(this)

        val gridLayoutManager = GridLayoutManager(this, 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1    // Movie_VIEW_TYPE will occupy 1 out of 3 span
                else return 3                                              // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        };


        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter

        viewModel.updateCart.observe(this) {
            cartDetails = it
            mCartItemCount = it.totalTickets
            setupBadge()
        }
        viewModel.setSortBy("")

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })

        // get cart details from here
        viewModel.getCartDetails()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu);
        menuInflater.inflate(R.menu.options, menu)
        val menuItem = menu!!.findItem(R.id.action_cart)

        val actionView = menuItem.actionView
        textCartItemCount = actionView!!.findViewById<View>(R.id.cart_badge) as TextView

        setupBadge()

        actionView.setOnClickListener { onOptionsItemSelected(menuItem) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menuSortRating -> {
                viewModel.setSortBy("vote_average.desc")
                true
            }
            R.id.menuSortTitle -> {
                viewModel.setSortBy("original_title.asc")
                true
            }
            R.id.menuSortDate -> {
                viewModel.setSortBy("primary_release_date.desc")
                true
            }
            R.id.menuSortPopularity -> {
                viewModel.setSortBy("popularity.desc")
                true
            }
            R.id.action_cart -> {
                if(this::cartDetails.isInitialized && cartDetails.totalTickets.toInt() > 0){
                    val intent = Intent(applicationContext, SingleMovie::class.java)
                    intent.putExtra("id", cartDetails.movieId.toInt())
                    intent.putExtra("total_ticket", cartDetails.totalTickets)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    applicationContext.startActivity(intent)
                }
                true
            }
            else -> {
                true
            }
        }
    }

    private fun getViewModel(): MainActivityViewModel {

        return ViewModelProviders.of(
            this,
            MyViewModelFactory(movieRepository, repository)
        )[MainActivityViewModel::class.java]
    }


    class MyViewModelFactory(
        private val repository: MoviePagedListRepository,
        private val movieLocalDBRepo: MovieLocalDBRepo
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(MainActivityViewModel::class.java!!)) {
                MainActivityViewModel(this.repository, this.movieLocalDBRepo) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }

    private fun setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount!!.visibility != View.GONE) {
                    textCartItemCount!!.visibility = View.GONE
                }
            } else {
                textCartItemCount!!.text = Math.min(mCartItemCount, 99).toString()
                if (textCartItemCount!!.visibility != View.VISIBLE) {
                    textCartItemCount!!.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        // get cart details from here
        viewModel.getCartDetails()
        super.onResume()

    }

}