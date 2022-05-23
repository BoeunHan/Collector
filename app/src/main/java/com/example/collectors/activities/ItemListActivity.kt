package com.example.collectors.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.collectors.Category
import com.example.collectors.R
import com.example.collectors.adapters.ItemAdapter
import com.example.collectors.database.BasicInfo
import com.example.collectors.database.MovieApp
import com.example.collectors.database.MovieDao
import com.example.collectors.textToFlow
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.activity_item_list.btCancel
import kotlinx.android.synthetic.main.activity_item_list.tvNothingFound
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_review_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ItemListActivity : AppCompatActivity() {

    private var movieDao: MovieDao? = null
    private lateinit var category: String

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        movieDao = (application as MovieApp).db.movieDao()
        category = intent.getStringExtra("Category") as String

        tvCategory.text = category
        fetchInfo("")

        btCancel.setOnClickListener {
            etSearchItem.setText("")
        }

        lifecycleScope.launch{
            val editTextFlow = etSearchItem.textToFlow()
            editTextFlow
                .debounce(500)
                .onEach{ fetchInfo(it.toString()) }
                .launchIn(this)
        }

        btAddItem.setOnClickListener(MyAddOnClickListener())
    }
    inner class MyAddOnClickListener : View.OnClickListener {
        override fun onClick(view: View?) {
            var intent: Intent? = null
            when(category){
                Category.MOVIE.name -> intent = Intent(this@ItemListActivity, MovieSearchActivity::class.java)
                Category.BOOK.name -> intent = Intent(this@ItemListActivity, MovieSearchActivity::class.java)
            }
            startActivity(intent)

        }
    }
    private fun fetchInfo(value: String){
        Log.e("string",value)
        when(category){
            Category.MOVIE.name -> {
                lifecycleScope.launch{
                    movieDao?.fetchBasicInfoSearch("%$value%")?.collect { list ->
                        val mySearchList = ArrayList<BasicInfo>()
                        for (item in list) mySearchList.add(item)
                        setItemAdapter(mySearchList)
                    }
                }
            }
            Category.BOOK.name -> {

            }
        }
    }

    private fun setItemAdapter(list: ArrayList<BasicInfo>){
        if(list.isNullOrEmpty()){
            tvNothingFound.visibility = View.VISIBLE
            rvItemList.visibility = View.GONE
        }
        else {
            tvNothingFound.visibility = View.GONE
            rvItemList.visibility = View.VISIBLE

            rvItemList.layoutManager = GridLayoutManager(this, 3)
            rvItemList.adapter = ItemAdapter(
                list, this@ItemListActivity,
                { deleteRecord(it) },
                { id, like -> likeOrDislike(id, like) }
            )
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