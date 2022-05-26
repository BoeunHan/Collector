package com.example.collectors.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.database.CollectorApp
import com.example.collectors.database.MovieEntity
import kotlinx.android.synthetic.main.activity_my_movie_detail.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MyMovieDetail : AppCompatActivity() {
    private var id: Int = 0
    private var myMovie: MovieEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_movie_detail)

        setSupportActionBar(toolbarMovieDetail)


        id = intent.getIntExtra(Constants.ID, 0)

        getMovieDetail()
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

    private fun getMovieDetail(){
        lifecycleScope.launch {
            val movieDao = (application as CollectorApp).db.movieDao()
            movieDao.fetchData(id).collect {
                myMovie = it
                setUI()
            }
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