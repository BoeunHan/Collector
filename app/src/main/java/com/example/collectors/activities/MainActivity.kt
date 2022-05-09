package com.example.collectors.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.collectors.R
import com.example.collectors.adapters.MyMovieAdapter
import com.example.collectors.database.MovieApp
import com.example.collectors.database.MovieDao
import com.example.collectors.database.MovieEntity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btAdd.setOnClickListener {
            val intent = Intent(this, MovieSearchActivity::class.java)
            startActivity(intent)
        }

        val movieDao = (application as MovieApp).db.movieDao()
        getMyMovies(movieDao)

    }

    private fun getMyMovies(movieDao: MovieDao){

        lifecycleScope.launch{
            movieDao.fetchAllMovies().collect { list ->
                if(list.isNotEmpty()){
                    val gridLayoutManager = GridLayoutManager(
                            this@MainActivity,
                            2,
                            GridLayoutManager.HORIZONTAL,
                            false)
                    rvMyMovieList.layoutManager = gridLayoutManager

                    val myMovieList = ArrayList<MovieEntity>()
                    for(movie in list) myMovieList.add(movie)

                    val myMovieAdapter = MyMovieAdapter(myMovieList, this@MainActivity)
                    rvMyMovieList.adapter = myMovieAdapter
                }
            }
        }
    }

}