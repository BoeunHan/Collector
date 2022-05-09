package com.example.collectors.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import com.bumptech.glide.Glide
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.database.MovieEntity
import kotlinx.android.synthetic.main.activity_my_movie_detail.*

class MyMovieDetail : AppCompatActivity() {

    private var myMovie: MovieEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_movie_detail)

        myMovie = intent.getParcelableExtra(Constants.SELECTED_MOVIE) as MovieEntity?

        setUI()

        btEdit.setOnClickListener{
            val intent = Intent(this, AddMovieActivity::class.java)
            intent.putExtra(Constants.SELECTED_MOVIE, myMovie)
            startActivity(intent)
            finish()
        }

    }

    private fun setUI(){
        Glide.with(this).load(myMovie?.image).into(ivMovieImage)
        collapsingToolbar.title = myMovie?.title
        ratingBar.rating = myMovie?.rate!!
        tvSummary.text = myMovie?.summary
        tvSummary.movementMethod = ScrollingMovementMethod()
        tvReview.text = myMovie?.review
        tvReview.movementMethod = ScrollingMovementMethod()
        tvUploadDate.text = "등록: ${myMovie?.uploadDate}"
        if(myMovie?.editDate!=""){
            tvEditDate.text = "수정: ${myMovie?.editDate}"
            tvEditDate.visibility = View.VISIBLE
        } else {
            tvEditDate.visibility = View.GONE
        }
    }

}