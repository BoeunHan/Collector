package com.example.collectors.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.database.MovieApp
import com.example.collectors.database.MovieDao
import com.example.collectors.database.MovieEntity
import kotlinx.android.synthetic.main.activity_add_movie.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddMovieActivity : AppCompatActivity(), View.OnClickListener {

    var movieImage: String? = null
    var movieTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        movieImage = intent.getStringExtra(Constants.MOVIE_IMAGE)
        movieTitle = intent.getStringExtra(Constants.MOVIE_TITLE)

        setUI()

        btSave.setOnClickListener(this)
    }
    private fun setUI(){
        Glide.with(this).load(movieImage).into(ivMovieImage)
        collapsingToolbar.title = movieTitle
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btAdd -> {}
            R.id.btSave -> {
                val rate = ratingBar.rating
                val summary = etSummary.text.toString()
                val review = etReview.text.toString()

                val myMovie = MovieEntity(
                        0,
                        movieTitle!!,
                        movieImage!!,
                        rate,
                        summary,
                        review
                )

                val movieDao = (application as MovieApp).db?.movieDao()
                addRecord(movieDao!!, myMovie)


                finish()
            }
        }
    }

    private fun addRecord(movieDao: MovieDao, movieEntity: MovieEntity){
        lifecycleScope.launch {
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd MMM, yyyy")
            val date = sdf.format(cal.time)
            movieEntity.uploadDate = date
            movieDao.insert(movieEntity)
        }
    }
}