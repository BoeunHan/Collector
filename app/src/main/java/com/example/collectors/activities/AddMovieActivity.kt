package com.example.collectors.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.database.CollectorApp
import com.example.collectors.database.MovieDao
import com.example.collectors.database.MovieEntity
import kotlinx.android.synthetic.main.activity_add_movie.*
import kotlinx.android.synthetic.main.activity_add_movie.collapsingToolbar
import kotlinx.android.synthetic.main.activity_add_movie.ivMovieImage
import kotlinx.android.synthetic.main.activity_add_movie.ratingBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddMovieActivity : AppCompatActivity() {

    var movieImage: String? = null
    var movieTitle: String? = null

    var myMovie: MovieEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        setSupportActionBar(toolbarMovieAddEdit)

        movieImage = intent.getStringExtra(Constants.MOVIE_IMAGE)
        movieTitle = intent.getStringExtra(Constants.MOVIE_TITLE)

        myMovie = intent.getParcelableExtra(Constants.SELECTED_MOVIE) as MovieEntity?

        setUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btSave -> {
                val rate = ratingBar.rating
                val summary = etSummary.text.toString()
                val review = etReview.text.toString()

                val date = Date()
                val sdf = SimpleDateFormat("dd MMM, yyyy")
                val datestr = sdf.format(date)

                val movieDao = (application as CollectorApp).db?.movieDao()

                if(myMovie==null){
                    val newMovie = MovieEntity(
                            0,
                            movieTitle!!,
                            movieImage!!,
                            rate,
                            summary,
                            review,
                            datestr
                    )
                    addRecord(movieDao, newMovie)
                }else{
                    val updateMovie = MovieEntity(
                            myMovie?.id!!,
                            myMovie?.title!!,
                            myMovie?.image!!,
                            rate,
                            summary,
                            review,
                            myMovie?.uploadDate!!,
                            datestr
                    )
                    updateRecord(movieDao, updateMovie)
                }

                finish()
            }
        }
        return true
    }

    private fun setUI(){
        if(myMovie==null) {
            Glide.with(this).load(movieImage).into(ivMovieImage)
            collapsingToolbar.title = movieTitle
        }else{
            Glide.with(this).load(myMovie?.image).into(ivMovieImage)
            collapsingToolbar.title = myMovie?.title
            ratingBar.rating = myMovie?.rate!!
            etSummary.setText(myMovie?.summary)
            etReview.setText(myMovie?.review)
        }
    }

    private fun addRecord(movieDao: MovieDao, movieEntity: MovieEntity){
        lifecycleScope.launch {
            movieDao.insert(movieEntity)
        }
    }

    private fun updateRecord(movieDao: MovieDao, movieEntity: MovieEntity){
        lifecycleScope.launch {
            movieDao.update(movieEntity)
        }
    }
}