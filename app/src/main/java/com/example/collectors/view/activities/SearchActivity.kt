package com.example.collectors.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collectors.utils.Constants
import com.example.collectors.view.adapters.MovieSearchAdapter
import com.example.collectors.databinding.ActivitySearchBinding
import com.example.collectors.model.data.networkModel.BookItem
import com.example.collectors.model.data.networkModel.MovieItem
import com.example.collectors.view.adapters.BookSearchAdapter
import com.example.collectors.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import java.util.*

@FlowPreview
@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private var rvSearchList: RecyclerView? = null

    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        category = intent.getStringExtra(Constants.CATEGORY)!!
        viewModel.category = category

        rvSearchList = binding.rvSearchList
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvSearchList?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rvSearchList?.layoutManager = layoutManager

        lifecycleScope.launch {
            when(category) {
                "MOVIE"-> viewModel.movieSearchResult.collect { list->
                    val arrayList = ArrayList<MovieItem>()
                    for(i in list) arrayList.add(i)
                    setupMovieSearchRecyclerView(arrayList)
                }
                "BOOK"-> viewModel.bookSearchResult.collect { list->
                    val arrayList = ArrayList<BookItem>()
                    for(i in list) arrayList.add(i)
                    setupBookSearchRecyclerView(arrayList)
                }
            }
        }

    }

    private fun setupMovieSearchRecyclerView(movieList: ArrayList<MovieItem>){
        if(movieList.isEmpty()) binding.isEmpty = true
        else {
            binding.isEmpty = false
            rvSearchList?.adapter = MovieSearchAdapter(movieList, this)
        }
    }
    private fun setupBookSearchRecyclerView(bookList: ArrayList<BookItem>){
        if(bookList.isEmpty()) binding.isEmpty = true
        else {
            binding.isEmpty = false
            rvSearchList?.adapter = BookSearchAdapter(bookList, this)
        }
    }

    fun onClickSearchItem(title: String, image: String){
        lifecycleScope.launch{
            if(viewModel.checkExist(title, image)){
                val str = when(category) {
                    "MOVIE" -> "영화"
                    "BOOK" -> "책"
                    else -> ""
                }
                Toast.makeText(this@SearchActivity, "이미 리뷰를 남긴 ${str}입니다.", Toast.LENGTH_SHORT).show()
            }else {
                lateinit var intent: Intent
                when(category){
                    "MOVIE" -> intent = Intent(this@SearchActivity, AddMovieActivity::class.java)
                    "BOOK" -> intent = Intent(this@SearchActivity, AddBookActivity::class.java)
                }
                intent.putExtra(Constants.IMAGE, image)
                intent.putExtra(Constants.TITLE, title)
                startActivity(intent)
                finish()
            }

        }
    }


}