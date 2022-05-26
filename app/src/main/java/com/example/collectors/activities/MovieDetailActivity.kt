package com.example.collectors.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.database.CollectorApp
import com.example.collectors.database.MovieDao
import com.example.collectors.database.MovieEntity
import kotlinx.android.synthetic.main.activity_my_movie_detail.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieDetailActivity : AppCompatActivity() {
    private var id: Int = 0
    private var myMovie: MovieEntity? = null
    private var movieDao: MovieDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_movie_detail)

        setSupportActionBar(toolbarMovieDetail)

        movieDao = (application as CollectorApp).db.movieDao()

        id = intent.getIntExtra(Constants.ID, 0)

        getMovieDetail()

        fabLike.setOnClickListener {
            if(myMovie?.like==true) {
                setLike(id, false)
                fabLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_dislike))
            }
            else {
                setLike(id, true)
                fabLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_like))
            }
        }
    }
    private fun setLike(id: Int, like: Boolean){
        lifecycleScope.launch{
            movieDao?.like(id, like)
        }
    }
    private fun getMovieDetail(){
        lifecycleScope.launch {
            movieDao?.fetchData(id)?.collect {
                myMovie = it
                setUI()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btEdit -> {
                val intent = Intent(this, AddMovieActivity::class.java)
                intent.putExtra(Constants.SELECTED_MOVIE, myMovie)
                startActivity(intent)
            }
        }
        return true
    }

    private fun setUI(){
        Glide.with(this).load(myMovie?.image).into(ivMovieImage)
        collapsingToolbar.title = myMovie?.title
        if(myMovie?.like==true) fabLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_like))
        else fabLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_dislike))
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