package com.han.collector.view.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.han.collector.utils.Constants
import com.han.collector.databinding.ActivitySearchBinding
import com.han.collector.model.data.remote.model.BookItem
import com.han.collector.model.data.remote.model.MovieItem
import com.han.collector.model.data.remote.model.PlaceItem
import com.han.collector.utils.BitmapCallback
import com.han.collector.utils.Converters
import com.han.collector.utils.FunctionUtils
import com.han.collector.view.adapters.BookSearchAdapter
import com.han.collector.view.adapters.MovieSearchAdapter
import com.han.collector.view.adapters.PlaceSearchAdapter
import com.han.collector.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

@FlowPreview
@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private var rvSearchList: RecyclerView? = null

    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activity = this
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        binding.etSearchMovie.filters = arrayOf(EMOJI_FLITER)

        category = intent.getStringExtra(Constants.CATEGORY)!!
        viewModel.category = category

        rvSearchList = binding.rvSearchList
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvSearchList?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rvSearchList?.layoutManager = layoutManager

        setRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        if (!Constants.isNetworkAvailable(this))
            Toast.makeText(this, "인터넷 연결 끊김", Toast.LENGTH_SHORT).show()
    }

    val EMOJI_FLITER = InputFilter { source, start, end, dst, dstart, dend ->
        for (i in start until end) {
            val type = Character.getType(source[i])
            if (type.toByte() == Character.SURROGATE) {
                return@InputFilter ""
            }
        }
        return@InputFilter null
    }

    private fun setRecyclerView() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchValueFlow.collectLatest {
                    rvSearchList?.scrollToPosition(0)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (category) {
                    "영화" -> {
                        val pagingAdapter = MovieSearchAdapter(this@SearchActivity)
                        pagingAdapter.addLoadStateListener { loadState ->
                            binding.isEmpty = (loadState.source.refresh is LoadState.NotLoading
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
                            binding.isEmpty = (loadState.source.refresh is LoadState.NotLoading
                                    && pagingAdapter.itemCount < 1)
                        }
                        rvSearchList?.adapter = pagingAdapter
                        viewModel.searchFlow.collectLatest { pagingData ->
                            pagingAdapter.submitData(pagingData as PagingData<BookItem>)
                        }
                    }
                    "장소" -> {
                        val pagingAdapter = PlaceSearchAdapter(this@SearchActivity)
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED) {
                                viewModel.searchValue.collectLatest {
                                    if (it.isEmpty()) binding.tvAddNewPlace.visibility = View.GONE
                                    else {
                                        binding.tvAddNewPlace.visibility = View.VISIBLE
                                        binding.tvAddNewPlace.text =
                                            if (it.length <= 10) "'$it' 직접 추가하기"
                                            else "'${it.substring(0..9)}...' 직접 추가하기"
                                    }
                                }
                            }
                        }
                        pagingAdapter.addLoadStateListener { loadState ->
                            binding.isEmpty = (loadState.source.refresh is LoadState.NotLoading
                                    && pagingAdapter.itemCount < 1)
                        }
                        rvSearchList?.adapter = pagingAdapter
                        viewModel.searchFlow.collectLatest { pagingData ->
                            pagingAdapter.submitData(pagingData as PagingData<PlaceItem>)
                        }
                    }
                }
            }
        }
    }

    fun onClickSearchItem(title: String, imageUrl: String) {
        val callback = object : BitmapCallback {
            override fun doWithBitmap(bitmap: Bitmap?) {
                lifecycleScope.launch {
                    if (viewModel.checkExist(title, bitmap)) {
                        Toast.makeText(
                            this@SearchActivity,
                            "이미 리뷰를 남긴 ${category}입니다.",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    } else {
                        val intent = Intent(this@SearchActivity, AddReviewActivity::class.java)
                        bitmap?.let {
                            val byteArray = Converters().toByteArray(bitmap)
                            intent.putExtra(Constants.IMAGE, byteArray)
                        }
                        intent.putExtra(Constants.TITLE, title)
                        intent.putExtra(Constants.CATEGORY, category)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
        FunctionUtils.loadBitmapFromUrl(this, imageUrl, callback)
    }

    val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(recyclerView.windowToken, 0)
        }
    }
}