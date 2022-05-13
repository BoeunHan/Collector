package com.example.collectors.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.collectors.R
import com.example.collectors.adapters.ReviewSearchAdapter
import com.example.collectors.database.MovieApp
import com.example.collectors.database.MovieDao
import com.example.collectors.database.MovieEntity
import com.example.collectors.textToFlow
import kotlinx.android.synthetic.main.activity_review_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class ReviewSearchActivity : AppCompatActivity() {

    private var reviewSearchAdapter: ReviewSearchAdapter? = null
    private var movieDao: MovieDao? = null

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_search)


        movieDao = (application as MovieApp).db.movieDao()

        btCancel.setOnClickListener {
            etSearchReview.setText("")
        }

        lifecycleScope.launch{
            val editTextFlow = etSearchReview.textToFlow()
            editTextFlow
                .debounce(500)
                .onEach{ getReviewSearch(it.toString()) }
                .launchIn(this)
        }
    }


    private fun getReviewSearch(value: String){
        lifecycleScope.launch{
            movieDao?.fetchSearchMovies("%$value%")?.collect { list ->
                val mySearchList = ArrayList<MovieEntity>()
                for (movie in list) mySearchList.add(movie)
                setupReviewSearchAdapter(mySearchList)
            }
        }
    }
    private fun setupReviewSearchAdapter(movieList: ArrayList<MovieEntity>){
        if(movieList.isNullOrEmpty()){
            tvNothingFound.visibility = View.VISIBLE
            rvReviewSearchList.visibility = View.GONE
        }
        else {
            tvNothingFound.visibility = View.GONE
            rvReviewSearchList.visibility = View.VISIBLE
            val gridLayoutManager = GridLayoutManager(
                    this@ReviewSearchActivity,
                    2,
                    GridLayoutManager.VERTICAL,
                    false)
            rvReviewSearchList.layoutManager = gridLayoutManager

            reviewSearchAdapter = ReviewSearchAdapter(
                    movieList,
                    this@ReviewSearchActivity
            ) { id, like -> likeOrDislike(id, like) }
            rvReviewSearchList.adapter = reviewSearchAdapter
        }

    }

    private fun likeOrDislike(id: Int, like: Boolean){
        lifecycleScope.launch{
            movieDao?.likeMovie(id, like)
        }
    }

}

