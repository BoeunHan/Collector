package com.example.collectors.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.bumptech.glide.Glide
import com.example.collectors.Constants
import com.example.collectors.R
import kotlinx.android.synthetic.main.activity_my_movie_detail.*

class MyMovieDetail : AppCompatActivity() {

    private var movieImage: String? = null
    private var movieTitle: String? = null
    private var movieRate: Float = 0.0f
    private var movieSummary: String? = null
    private var movieReview: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_movie_detail)

        movieImage = intent.getStringExtra(Constants.MOVIE_IMAGE)
        movieTitle = intent.getStringExtra(Constants.MOVIE_TITLE)
        movieRate = intent.getFloatExtra(Constants.MOVIE_RATE, 0.0f)
        movieSummary = intent.getStringExtra(Constants.MOVIE_SUMMARY)
        movieReview = intent.getStringExtra(Constants.MOVIE_REVIEW)

        setUI()
    }

    private fun setUI(){
        Glide.with(this).load(movieImage).into(ivMovieImage)
        collapsingToolbar.title = movieTitle
        ratingBar.rating = movieRate
        tvSummary.text = movieSummary
        tvSummary.movementMethod = ScrollingMovementMethod()
        tvReview.text = movieReview
        tvReview.movementMethod = ScrollingMovementMethod()

    }

}