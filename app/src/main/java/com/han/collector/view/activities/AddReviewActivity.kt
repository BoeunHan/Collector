package com.han.collector.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.han.collector.utils.Constants
import com.han.collector.R
import com.han.collector.databinding.ActivityAddReviewBinding
import com.han.collector.view.fragments.AddBookFragment
import com.han.collector.view.fragments.AddMovieFragment
import com.han.collector.viewmodel.BookViewModel
import com.han.collector.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReviewBinding
    private val movieViewModel: MovieViewModel by viewModels()
    private val bookViewModel: BookViewModel by viewModels()

    private var itemImage: String? = null
    private var itemTitle: String? = null

    private var id: Int? = null
    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMovieAddEdit)

        binding.lifecycleOwner = this

        itemImage = intent.getStringExtra(Constants.IMAGE)
        itemTitle = intent.getStringExtra(Constants.TITLE)
        category = intent.getStringExtra(Constants.CATEGORY)

        id = intent.getIntExtra(Constants.SELECTED_ID,0)

        binding.image = itemImage
        binding.title = itemTitle

        if(savedInstanceState == null){
            when(category) {
                "영화" -> supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<AddMovieFragment>(binding.addFragmentContainerView.id)
                }
                "책" -> supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<AddBookFragment>(binding.addFragmentContainerView.id)
                }
            }
        }

        setData()
    }

    private fun setData(){
        when(category){
            "영화" -> {
                if(id==0) movieViewModel.setMovieStatus(itemTitle!!,itemImage!!)
                else movieViewModel.setMovieStatus(id!!)
            }
            "책" -> {
                if(id==0) bookViewModel.setBookStatus(itemTitle!!,itemImage!!)
                else bookViewModel.setBookStatus(id!!)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btSave -> {
                when(category){
                    "영화" -> movieViewModel.saveMovie()
                    "책" -> bookViewModel.saveBook()
                }
                finish()
            }
        }
        return true
    }

}