package com.han.collector.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.han.collector.utils.Constants
import com.han.collector.R
import com.han.collector.databinding.ActivityMovieDetailBinding
import com.han.collector.model.data.database.MovieEntity
import com.han.collector.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private val viewModel: ItemViewModel by viewModels()
    private var id: Int? = null
    private var myMovie: MovieEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMovieDetail)

        binding.activity = this
        binding.lifecycleOwner = this

        id = intent.getIntExtra(Constants.SELECTED_ID,0)
        lifecycleScope.launch {
            viewModel.getMovieDetail(id!!).collectLatest {
                myMovie = it
                binding.item = myMovie
            }
        }

        binding.tvSummary.movementMethod = ScrollingMovementMethod()
        binding.tvReview.movementMethod = ScrollingMovementMethod()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btEdit -> {
                val intent = Intent(this, AddMovieActivity::class.java)
                intent.putExtra(Constants.SELECTED_MOVIE, myMovie)
                startActivity(intent)
            }
        }
        return true
    }

    fun setLike(view: View){
        lifecycleScope.launch {
            if (myMovie!!.like) viewModel.setMovieLike(myMovie!!.id, false)
            else viewModel.setMovieLike(myMovie!!.id, true)
        }
    }

}