package com.example.collectors.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collectors.Category
import com.example.collectors.Categoryy
import com.example.collectors.R
import com.example.collectors.adapters.MainAdapter
import com.example.collectors.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var movieDao: MovieDao? = null
    private var categoryList = ArrayList<String>()
    private lateinit var mainList: ArrayList<Categoryy>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categoryList.add("MOVIE")
        categoryList.add("BOOK")

        movieDao = (application as MovieApp).db.movieDao()

        setMainList()
    }
    private fun setMainList() {
        mainList = ArrayList()
        for (i in categoryList) {
            when (i) {
                "MOVIE" -> {
                    lifecycleScope.launch {
                        movieDao?.fetchAllBasicInfo()?.collect { list ->
                            val movieList = ArrayList<BasicInfo>()
                            for (movie in list) movieList.add(movie)

                            mainList.add(Categoryy("MOVIE", movieList, movieDao as BaseDao<BaseEntity>))
                        }

                    }
                }
                "BOOK" -> {
                    lifecycleScope.launch {
                        movieDao?.fetchAllBasicInfo()?.collect { list ->
                            val movieList = ArrayList<BasicInfo>()
                            for (movie in list) movieList.add(movie)
                            mainList.add(Categoryy("BOOK", movieList, movieDao as BaseDao<BaseEntity>))
                        }
                    }
                }
            }
        }
        for (i in mainList) {
            Log.e("mainlist: ", i.category)
        }
        setMainAdapter()
    }

    private fun setMainAdapter(){
        rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
        rvMain.adapter = MainAdapter(
                mainList,this@MainActivity
        )


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