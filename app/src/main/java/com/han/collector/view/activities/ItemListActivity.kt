package com.han.collector.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.han.collector.utils.Constants
import com.han.collector.R
import com.han.collector.model.data.database.BasicInfo
import com.han.collector.databinding.ActivityItemListBinding
import com.han.collector.databinding.RemoveDialogBinding
import com.han.collector.databinding.SortDialogBinding
import com.han.collector.view.adapters.ItemAdapter
import com.han.collector.viewmodel.ItemViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class ItemListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemListBinding
    private val viewModel: ItemViewModel by viewModels()

    private var rvItemList: RecyclerView? = null
    private var removeDialog: BottomSheetDialog? = null
    private var itemAdapter: ItemAdapter? = null

    private lateinit var sortDialog: BottomSheetDialog

    private lateinit var category: String

    @OptIn(ExperimentalCoroutinesApi::class)
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

        lifecycleScope.launch {
            viewModel.itemList.collect {
                val arrayList = ArrayList(it)
                setItemAdapter(arrayList)
            }
        }

    }


    private fun setItemAdapter(list: ArrayList<BasicInfo>) {
        if (list.isEmpty()) {
            binding.isEmpty = true
        } else {
            binding.isEmpty = false
            itemAdapter = ItemAdapter(
                list, category, this@ItemListActivity, viewModel
            )
            rvItemList?.adapter = itemAdapter
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
        removeDialog?.dismiss()
    }


    fun searchItems(category: String) {
        viewModel.setSelectMode(false)
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(Constants.CATEGORY, category)
        startActivity(intent)
    }

    fun getItemDetail(category: String, id: Int) {
        val intent = Intent(this@ItemListActivity, ReviewDetailActivity::class.java)
        intent.putExtra(Constants.CATEGORY, category)
        intent.putExtra(Constants.SELECTED_ID, id)
        startActivity(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
        viewModel.setSelectMode(false)
    }


}