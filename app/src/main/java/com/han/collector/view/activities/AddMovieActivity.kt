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
import com.han.collector.model.data.database.MovieEntity
import com.han.collector.databinding.ActivityAddMovieBinding
import com.han.collector.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMovieActivity : AppCompatActivity() {

    var movieImage: String? = null
    var movieTitle: String? = null

    var myMovie: MovieEntity? = null

    private lateinit var binding: ActivityAddMovieBinding
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMovieAddEdit)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        movieImage = intent.getStringExtra(Constants.IMAGE)       //add의 경우
        movieTitle = intent.getStringExtra(Constants.TITLE)

        myMovie = intent.getParcelableExtra(Constants.SELECTED_MOVIE)       //edit의 경우

        if(myMovie==null) viewModel.setMovieStatus(movieTitle!!, movieImage!!)
        else viewModel.setMovieStatus(myMovie!!)
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