package com.example.collectors.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collectors.utils.Constants
import com.example.collectors.R
import com.example.collectors.model.data.database.BasicInfo
import com.example.collectors.databinding.ActivityItemListBinding
import com.example.collectors.databinding.RemoveDialogBinding
import com.example.collectors.databinding.SortDialogBinding
import com.example.collectors.view.adapters.ItemAdapter
import com.example.collectors.viewmodel.ItemViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
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
        viewModel.setCategory(category)
        binding.category = category

        lifecycleScope.launch {

            viewModel.itemList.collect {
                val arrayList = ArrayList<BasicInfo>()
                for(i in it) arrayList.add(i)
                setItemAdapter(arrayList)
            }
        }

    }


    private fun setItemAdapter(list: ArrayList<BasicInfo>) {
        if (list.isEmpty()) {
            binding.isEmpty = true
        } else {
            binding.isEmpty = false
            itemAdapter = ItemAdapter(list, category, this@ItemListActivity, viewModel)
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
        if (view.id == R.id.btRemoveCheck) {
            viewModel.removeSelectedItems()
        }
        removeDialog?.dismiss()
    }



    fun searchItems(category: String) {
        var intent: Intent? = null
        when (category) {
            "MOVIE" -> {
                intent = Intent(this, SearchActivity::class.java)
            }
            "BOOK" -> {
                //intent = Intent(this, MovieSearchActivity::class.java)
            }
        }
        intent?.putExtra(Constants.CATEGORY, category)
        startActivity(intent)
    }

    fun getItemDetail(category: String, id: Int){
        when (category) {
            "MOVIE" -> {/*
                val intent = Intent(this, MovieDetailActivity::class.java)
                intent.putExtra(Constants.ID, id)
                startActivity(intent)*/
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.setSelectMode(false)
    }

    override fun onDestroy() {
        super.onDestroy()
       //viewModel.setMainMode(true)
        viewModel.clear()
    }
}