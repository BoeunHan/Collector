package com.example.collectors.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.adapters.MainAdapter
import com.example.collectors.adapters.MyMovieAdapter
import com.example.collectors.database.BasicInfo
import com.example.collectors.database.MovieApp
import com.example.collectors.database.MovieDao
import com.example.collectors.database.MovieEntity
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.main_list_item_view.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private var mainAdapter: MainAdapter? = null
    private var movieDao: MovieDao? = null
    private val mainList: ArrayList<Pair<String, ArrayList<BasicInfo>>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        movieDao = (application as MovieApp).db.movieDao()
        fetchMainInfo()
        //getMyMovies()

        /*
        btAdd.setOnClickListener {
            val intent = Intent(this, MovieSearchActivity::class.java)
            startActivity(intent)
        }

        btRemove.setOnClickListener {
            Constants.isRemoveMode = !Constants.isRemoveMode
            myMovieAdapter?.notifyDataSetChanged()
        }

        btSearchReview.setOnClickListener {
            val intent = Intent(this, ReviewSearchActivity::class.java)
            startActivity(intent)
        }*/
    }

    private fun fetchMainInfo(){
        lifecycleScope.launch{
            val myList = ArrayList<BasicInfo>()
            movieDao?.fetchBasicInfo()?.collect { list ->

                for (movie in list) myList.add(movie)
                setMainAdapter(mainList)
            }
            mainList.add(Pair("영화",myList))


        }
    }

    private fun setMainAdapter(mainList: ArrayList<Pair<String, ArrayList<BasicInfo>>>){
        if(mainList.isNullOrEmpty()){
            tvNothingFound.visibility = View.VISIBLE
            rvMain.visibility = View.GONE
        }
        else {
            Log.e("setMainAdapter","adapter set")
            Log.e("mainlist",mainList.toString())
            tvNothingFound.visibility = View.GONE
            rvMain.visibility = View.VISIBLE

            val linearLayoutManager = LinearLayoutManager(this@MainActivity)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            rvMain.layoutManager = linearLayoutManager
            rvMain.adapter = MainAdapter(
                mainList,
                this@MainActivity,
                { deleteRecord(it) },
                { id, like -> likeOrDislike(id, like) }
            )
        }
    }
/*
    private fun getMyMovies(){
        lifecycleScope.launch{
            movieDao?.fetchAllMovies()?.collect { list ->
                val myMovieList = ArrayList<MovieEntity>()
                for (movie in list) myMovieList.add(movie)
                setMovieAdapter(myMovieList)
            }
        }
    }

    private fun setMovieAdapter(myMovieList: ArrayList<MovieEntity>){
        if(myMovieList.isNullOrEmpty()){
            tvNothingFound.visibility = View.VISIBLE
            rvMyMovieList.visibility = View.GONE
        }
        else {
            tvNothingFound.visibility = View.GONE
            rvMyMovieList.visibility = View.VISIBLE
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
                    { id, like -> likeOrDislike(id, like) }
            )
            rvMyMovieList.adapter = myMovieAdapter
        }
    }
*/
    private fun deleteRecord(id: Int){
        lifecycleScope.launch{
            movieDao?.delete(id)
        }
    }
    private fun likeOrDislike(id: Int, like: Boolean){
        lifecycleScope.launch{
            movieDao?.likeMovie(id, like)
        }
    }

}