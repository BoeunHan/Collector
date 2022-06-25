package com.han.collector.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.han.collector.utils.Constants
import com.han.collector.R
import com.han.collector.databinding.ActivityAddBookBinding
import com.han.collector.model.data.database.BookEntity
import com.han.collector.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBookActivity : AppCompatActivity() {

    var bookImage: String? = null
    var bookTitle: String? = null

    var myBook: BookEntity? = null

    private lateinit var binding: ActivityAddBookBinding
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMovieAddEdit)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        bookImage = intent.getStringExtra(Constants.IMAGE)       //add의 경우
        bookTitle = intent.getStringExtra(Constants.TITLE)

        myBook = intent.getParcelableExtra(Constants.SELECTED_BOOK)       //edit의 경우

        if(myBook==null) viewModel.setBookStatus(bookTitle!!, bookImage!!)
        else viewModel.setBookStatus(myBook!!)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btSave -> {
                viewModel.onClickSave()
                finish()
            }
        }
        return true
    }
}