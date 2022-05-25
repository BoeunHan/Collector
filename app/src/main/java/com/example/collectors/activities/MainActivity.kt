package com.example.collectors.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collectors.R
import com.example.collectors.adapters.MainAdapter
import com.example.collectors.adapters.SecondAdapter
import com.example.collectors.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private var movieDao: MovieDao? = null
    private var categoryList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categoryList.add("MOVIE")
        categoryList.add("BOOK")

        movieDao = (application as CollectorApp).db.movieDao()

        setMainAdapter()
    }


    private fun setMainAdapter(){
        rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
        rvMain.adapter = MainAdapter(
                categoryList,
                this@MainActivity
        ) { category, recyclerView -> setCategoryList(category, recyclerView) }

    }


    private fun setCategoryList(category: String, recyclerView: RecyclerView){
        when(category){
            "MOVIE" -> {
                lifecycleScope.launch{
                    movieDao?.fetchAllBasicInfo()?.collect { list ->
                        val movieList = ArrayList<BasicInfo>()
                        for (movie in list) movieList.add(movie)
                        setAdapter(movieList, recyclerView)
                    }
                }
            }
            "BOOK" -> {
                setAdapter(ArrayList(), recyclerView)
            }
        }
    }
    private fun setAdapter(list: ArrayList<BasicInfo>, recyclerView: RecyclerView){
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.adapter = SecondAdapter(
                list, this@MainActivity,
                {it},{id, like ->{}})
    }

    private fun deleteRecord(id: Int){
        lifecycleScope.launch{
            movieDao?.delete(id)
        }
    }
    private fun likeOrDislike(id: Int, like: Boolean){
        lifecycleScope.launch{
            movieDao?.like(id, like)
        }
    }

}