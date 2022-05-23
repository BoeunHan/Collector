package com.example.collectors.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collectors.Category
import com.example.collectors.R
import com.example.collectors.adapters.MainAdapter
import com.example.collectors.database.BasicInfo
import com.example.collectors.database.MovieApp
import com.example.collectors.database.MovieDao
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var movieDao: MovieDao? = null
    private var categoryList = ArrayList<Category>()

    private val LAYOUT_IDS = arrayOf(R.id.llMovieMain, R.id.llBookMain)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categoryList.add(Category.MOVIE)
        categoryList.add(Category.BOOK)
        movieDao = (application as MovieApp).db.movieDao()
        setUI()
        setButton()
    }
    private fun setUI() {
        if (categoryList.isNullOrEmpty()) {
            tvNothingFound.visibility = View.VISIBLE
            for(id in LAYOUT_IDS) (findViewById<LinearLayout>(id)).visibility = View.GONE
        } else {
            tvNothingFound.visibility = View.GONE
            fetchMainInfo()
        }
    }
    private fun fetchMainInfo(){
        for(i in categoryList){
            when(i){
                Category.MOVIE -> {
                    lifecycleScope.launch{
                        movieDao?.fetchBasicInfo()?.collect { list ->
                            val myList = ArrayList<BasicInfo>()
                            for (movie in list) myList.add(movie)
                            setMainAdapter(Category.MOVIE, myList)
                        }
                    }
                }
                Category.BOOK -> {
                    lifecycleScope.launch{
                        val myList = ArrayList<BasicInfo>()
                        setMainAdapter(Category.BOOK, myList)
                    }
                }
            }
        }
    }

    private fun setMainAdapter(category: Category, list: ArrayList<BasicInfo>){
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        when(category){
            Category.MOVIE -> {
                if(list.isNullOrEmpty()) llMovieMain.visibility = View.GONE
                else {
                    llMovieMain.visibility = View.VISIBLE
                    rvMovieList.layoutManager = linearLayoutManager
                    rvMovieList.adapter = MainAdapter(
                        Category.MOVIE, list,this@MainActivity,
                        { deleteRecord(it) },
                        { id, like -> likeOrDislike(id, like) }
                    )
                }
            }
            Category.BOOK -> {
                if(list.isNullOrEmpty()) llBookMain.visibility = View.GONE
                else {
                    llBookMain.visibility = View.VISIBLE
                    rvBookList.layoutManager = linearLayoutManager
                    rvBookList.adapter = MainAdapter(
                        Category.BOOK, list,this@MainActivity,
                        { deleteRecord(it) },
                        { id, like -> likeOrDislike(id, like) }
                    )
                }
            }
        }
    }

    private fun setButton() {
        btAddMovie.setOnClickListener(MyAddOnClickListener(Category.MOVIE))
        btMoreMovie.setOnClickListener(MyMoreOnClickListener(Category.MOVIE))
        btAddBook.setOnClickListener(MyAddOnClickListener(Category.BOOK))
        btMoreBook.setOnClickListener(MyMoreOnClickListener(Category.BOOK))

    }

    inner class MyAddOnClickListener(private val category: Category) : View.OnClickListener {
        override fun onClick(view: View?) {
            var intent: Intent? = null
            when(category){
                Category.MOVIE -> intent = Intent(this@MainActivity, MovieSearchActivity::class.java)
                Category.BOOK -> intent = Intent(this@MainActivity, MovieSearchActivity::class.java)
            }
            startActivity(intent)
        }
    }
    inner class MyMoreOnClickListener(private val category: Category) : View.OnClickListener {
        override fun onClick(view: View?) {
            val intent = Intent(this@MainActivity, ItemListActivity::class.java)
            when(category){
                Category.MOVIE -> intent.putExtra("Category", Category.MOVIE.name)
                Category.BOOK -> intent.putExtra("Category", Category.BOOK.name)
            }
            startActivity(intent)
        }
    }

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