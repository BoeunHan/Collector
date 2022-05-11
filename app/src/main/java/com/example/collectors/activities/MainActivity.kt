package com.example.collectors.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.adapters.MyMovieAdapter
import com.example.collectors.database.MovieApp
import com.example.collectors.database.MovieDao
import com.example.collectors.database.MovieEntity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private var myMovieAdapter: MyMovieAdapter? = null
    private var movieDao: MovieDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btAdd.setOnClickListener {
            val intent = Intent(this, MovieSearchActivity::class.java)
            startActivity(intent)
        }



        movieDao = (application as MovieApp).db.movieDao()
        getMyMovies()
        btRemove.setOnClickListener {
            Constants.isRemoveMode = !Constants.isRemoveMode
            myMovieAdapter?.notifyDataSetChanged()
        }
    }

    private fun getMyMovies(){

        lifecycleScope.launch{
            movieDao?.fetchAllMovies()?.collect { list ->
                if(list.isNotEmpty()){
                    val myMovieList = ArrayList<MovieEntity>()
                    for(movie in list) myMovieList.add(movie)
                    setMovieAdapter(myMovieList)
                }
            }
        }
    }

    private fun setMovieAdapter(myMovieList: ArrayList<MovieEntity>){
        val gridLayoutManager = GridLayoutManager(
                this@MainActivity,
                2,
                GridLayoutManager.HORIZONTAL,
                false)
        rvMyMovieList.layoutManager = gridLayoutManager

        myMovieAdapter = MyMovieAdapter(
                myMovieList,
                this@MainActivity,
                { deleteRecord(it) },
                { id, like -> likeOrDislike(id, like)}
        )
        rvMyMovieList.adapter = myMovieAdapter
    }

    private fun deleteRecord(movieEntity: MovieEntity){
        lifecycleScope.launch{
            movieDao?.delete(movieEntity)
        }
    }
    private fun likeOrDislike(id: Int, like: Boolean){
        lifecycleScope.launch{
            movieDao?.likeMovie(id, like)
        }
    }
}