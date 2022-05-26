package com.example.collectors.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.adapters.ItemAdapter
import com.example.collectors.database.BasicInfo
import com.example.collectors.database.CollectorApp
import com.example.collectors.database.MovieDao
import com.example.collectors.textToFlow
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.activity_item_list.btCancel
import kotlinx.android.synthetic.main.activity_item_list.tvNothingFound
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_list_item_view.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ItemListActivity : AppCompatActivity() {

    private var movieDao: MovieDao? = null
    private var category: String? = null

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        movieDao = (application as CollectorApp).db.movieDao()
        category = intent.getStringExtra(Constants.CATEGORY)

        tvListCategory.text = category

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

        btAddItem.setOnClickListener {
            var intent: Intent? = null
            when(category){
                "MOVIE" -> { intent = Intent(this, MovieSearchActivity::class.java) }
                "BOOK" -> { intent = Intent(this, MovieSearchActivity::class.java) }
            }
            intent?.putExtra("Category", category)
            startActivity(intent)
        }
    }

    private fun fetchInfo(value: String){
        when(category){
            "MOVIE" -> {
                lifecycleScope.launch{
                    movieDao?.searchBasicInfo("%$value%")?.collect { list ->
                        val mySearchList = ArrayList<BasicInfo>()
                        for (item in list) mySearchList.add(item)
                        setItemAdapter(mySearchList)
                    }
                }
            }
            "BOOK" -> {
                setItemAdapter(ArrayList())
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
                    category!!,
                    list, this@ItemListActivity
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
            movieDao?.like(id, like)
        }
    }
}