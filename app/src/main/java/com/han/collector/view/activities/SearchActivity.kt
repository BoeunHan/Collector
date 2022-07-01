package com.han.collector.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.han.collector.utils.Constants
import com.han.collector.databinding.ActivitySearchBinding
import com.han.collector.model.data.networkModel.BookItem
import com.han.collector.model.data.networkModel.MovieItem
import com.han.collector.view.adapters.BookSearchAdapter
import com.han.collector.view.adapters.MovieSearchAdapter
import com.han.collector.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private var rvSearchList: RecyclerView? = null

    private lateinit var category: String

    @OptIn(ExperimentalCoroutinesApi::class)
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
            when (category) {
                "영화" -> {
                    val pagingAdapter = MovieSearchAdapter(this@SearchActivity)
                    pagingAdapter.addLoadStateListener { loadState ->
                        binding.isEmpty =
                            (loadState.source.refresh is LoadState.NotLoading
                                    && pagingAdapter.itemCount < 1)
                    }
                    rvSearchList?.adapter = pagingAdapter
                    viewModel.searchFlow.collectLatest { pagingData ->
                        pagingAdapter.submitData(pagingData as PagingData<MovieItem>)
                    }
                }
                "책" -> {
                    val pagingAdapter = BookSearchAdapter(this@SearchActivity)
                    pagingAdapter.addLoadStateListener { loadState ->
                        binding.isEmpty =
                            (loadState.source.refresh is LoadState.NotLoading
                                    && pagingAdapter.itemCount < 1)
                    }
                    rvSearchList?.adapter = pagingAdapter
                    viewModel.searchFlow.collectLatest { pagingData ->
                        pagingAdapter.submitData(pagingData as PagingData<BookItem>)
                    }
                }
            }

        }
    }

    fun onClickSearchItem(title: String, image: String) {
        lifecycleScope.launch {
            if (viewModel.checkExist(title, image)) {
                Toast.makeText(this@SearchActivity, "이미 리뷰를 남긴 ${category}입니다.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val intent = Intent(this@SearchActivity, AddReviewActivity::class.java)

                intent.putExtra(Constants.IMAGE, image)
                intent.putExtra(Constants.TITLE, title)
                intent.putExtra(Constants.CATEGORY, category)
                startActivity(intent)
                finish()
            }
        }
    }


}