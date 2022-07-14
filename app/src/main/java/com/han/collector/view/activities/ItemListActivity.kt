package com.han.collector.view.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.han.collector.utils.Constants
import com.han.collector.R
import com.han.collector.databinding.ActivityItemListBinding
import com.han.collector.databinding.RemoveDialogBinding
import com.han.collector.databinding.SortDialogBinding
import com.han.collector.viewmodel.ItemViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.han.collector.view.adapters.ItemAdapter
import com.han.collector.view.fragments.ReviewDetailDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class ItemListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemListBinding
    val viewModel: ItemViewModel by viewModels()

    private var rvItemList: RecyclerView? = null
    private var removeDialog: BottomSheetDialog? = null

    private lateinit var sortDialog: BottomSheetDialog

    private lateinit var category: String

    private var pagingAdapter: ItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activity = this
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        rvItemList = binding.rvItemList
        rvItemList?.layoutManager = GridLayoutManager(this, 3)


        category = intent.getStringExtra(Constants.CATEGORY)!!
        viewModel.category = category
        binding.category = category

        setRecyclerView()
    }

    private fun setRecyclerView() {
        pagingAdapter = ItemAdapter(category, this@ItemListActivity)
        pagingAdapter?.addLoadStateListener { loadState ->
            binding.isEmpty =
                (loadState.source.refresh is LoadState.NotLoading
                        && pagingAdapter?.itemCount!! < 1)
        }

        val smoothScroller = object : LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = 0

        binding.rvItemList.adapter = pagingAdapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.itemFlow.collectLatest {
                    pagingAdapter?.submitData(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sortFlow.collectLatest {
                    //rvItemList?.layoutManager?.smoothScrollToPosition(rvItemList, null, 0)
                    //rvItemList?.layoutManager?.startSmoothScroll(smoothScroller)
                    rvItemList?.smoothScrollToPosition(0)
                    //rvItemList?.layoutManager?.scrollToPosition(0)

                }
            }
        }
    }

    fun showSortDialog(view: View) {
        sortDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialog)
        val binding = SortDialogBinding.inflate(layoutInflater)
        binding.activity = this
        sortDialog.setContentView(binding.root)
        sortDialog.setCancelable(true)
        sortDialog.show()
    }

    fun onClickSortDialog(view: View) {
        viewModel.setMode(view)
        sortDialog.dismiss()
    }


    fun showRemoveDialog(view: View) {
        removeDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialog)
        val binding = RemoveDialogBinding.inflate(layoutInflater)
        binding.activity = this
        removeDialog?.setContentView(binding.root)
        removeDialog?.setCancelable(true)
        removeDialog?.show()
    }

    fun onClickRemoveDialog(view: View) {
        if (view.id == R.id.btRemoveCheck) viewModel.removeIdSet()
        pagingAdapter?.refresh()
        removeDialog?.dismiss()
    }


    fun searchItems(category: String) {
        viewModel.setSelectMode(false)
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(Constants.CATEGORY, category)
        startActivity(intent)
    }

    fun getItemDetail(category: String, id: Int) {
        val cardDialog = ReviewDetailDialogFragment()
        val bundle = Bundle()
        bundle.putString(Constants.CATEGORY, category)
        bundle.putInt(Constants.SELECTED_ID, id)
        cardDialog.arguments = bundle
        cardDialog.show(supportFragmentManager, ReviewDetailDialogFragment.TAG)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
        viewModel.setSelectMode(false)
    }

    val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(recyclerView.windowToken, 0)
        }
    }
}