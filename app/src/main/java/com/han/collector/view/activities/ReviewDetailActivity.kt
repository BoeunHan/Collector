package com.han.collector.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import com.han.collector.utils.Constants
import com.han.collector.R
import com.han.collector.databinding.ActivityReviewDetailBinding
import com.han.collector.view.fragments.BookDetailFragment
import com.han.collector.view.fragments.MovieDetailFragment
import com.han.collector.viewmodel.BookViewModel
import com.han.collector.viewmodel.ItemViewModel
import com.han.collector.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class ReviewDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewDetailBinding
    private val viewModel: ItemViewModel by viewModels()
    private val movieViewModel: MovieViewModel by viewModels()
    private val bookViewModel: BookViewModel by viewModels()

    private var id: Int? = null
    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviewDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMovieDetail)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        category = intent.getStringExtra(Constants.CATEGORY)
        viewModel.category = category!!
        id = intent.getIntExtra(Constants.SELECTED_ID, 0)
        binding.id = id

        getData()

        if (savedInstanceState == null) {
            when (category) {
                "영화" -> supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<MovieDetailFragment>(
                        binding.detailFragmentContainerView.id
                    )
                }
                "책" -> supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<BookDetailFragment>(
                        binding.detailFragmentContainerView.id
                    )
                }
            }
        }
    }

    private fun getData() {
        lifecycleScope.launch {
            when (category) {
                "영화" -> {
                    movieViewModel.getMovieDetail(id!!)
                    movieViewModel.movieDetail.collectLatest {
                        binding.title = it.title
                        binding.image = it.image
                        binding.like = it.like
                    }
                }
                "책" -> {
                    bookViewModel.getBookDetail(id!!)
                    bookViewModel.bookDetail.collectLatest {
                        binding.title = it.title
                        binding.image = it.image
                        binding.like = it.like
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btEdit -> {
                val intent = Intent(this, AddReviewActivity::class.java)
                intent.putExtra(Constants.SELECTED_ID, id)
                intent.putExtra(Constants.CATEGORY, category)
                intent.putExtra(Constants.IMAGE, binding.image)
                intent.putExtra(Constants.TITLE, binding.title)
                startActivity(intent)
            }
        }
        return true
    }

}