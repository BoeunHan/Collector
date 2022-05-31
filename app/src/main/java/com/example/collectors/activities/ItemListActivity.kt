package com.example.collectors.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.adapters.ItemAdapter
import com.example.collectors.database.BasicInfo
import com.example.collectors.database.CollectorApp
import com.example.collectors.database.MovieDao
import com.example.collectors.textToFlow
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.activity_item_list.btCancel
import kotlinx.android.synthetic.main.activity_item_list.tvNothingFound
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_list_item_view.view.*
import kotlinx.android.synthetic.main.remove_dialog.*
import kotlinx.android.synthetic.main.sort_dialog.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ItemListActivity : AppCompatActivity(), View.OnClickListener {

    private var movieDao: MovieDao? = null
    private var category: String? = null
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var sortJob: Job
    private var sortNum: Int = 1
    private var curString: String = ""

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        movieDao = (application as CollectorApp).db.movieDao()
        category = intent.getStringExtra(Constants.CATEGORY)

        tvListCategory.text = category

        sortJob = orderDateDescending("")

        btCancel.setOnClickListener {
            etSearchItem.setText("")
        }

        lifecycleScope.launch{
            val editTextFlow = etSearchItem.textToFlow()
            editTextFlow
                .debounce(500)
                .onEach{
                    curString = it.toString()
                    fetchInfo(curString)
                }
                .launchIn(this)
        }

        Constants.selectMode = false
        btSelect.text = "선택"

        btAddItem.setOnClickListener(this)
        btSelect.setOnClickListener(this)
        fabRemove.setOnClickListener(this)
        btSort.setOnClickListener(this)
        btSortImage.setOnClickListener(this)
    }

    private fun fetchInfo(value: String){
        sortJob.cancel()
        when(sortNum) {
            1 -> sortJob = orderDateDescending(value)
            2-> sortJob = orderDateAscending(value)
            3 -> sortJob = orderRateDescending(value)
            4 -> sortJob = orderRateAscending(value)
        }
    }
    private fun orderDateDescending(value: String): Job{
        return lifecycleScope.launch{
            movieDao?.searchBasicInfoDateDescending("%$value%")?.collect { list ->
                val mySearchList = ArrayList<BasicInfo>()
                for (item in list) mySearchList.add(item)
                setItemAdapter(mySearchList)
            }
        }
    }
    private fun orderDateAscending(value: String): Job{
        return lifecycleScope.launch{
            movieDao?.searchBasicInfoDateAscending("%$value%")?.collect { list ->
                val mySearchList = ArrayList<BasicInfo>()
                for (item in list) mySearchList.add(item)
                setItemAdapter(mySearchList)
            }
        }
    }
    private fun orderRateDescending(value: String): Job{
        return lifecycleScope.launch{
            movieDao?.searchBasicInfoRateDescending("%$value%")?.collect { list ->
                val mySearchList = ArrayList<BasicInfo>()
                for (item in list) mySearchList.add(item)
                setItemAdapter(mySearchList)
            }
        }
    }
    private fun orderRateAscending(value: String): Job{
        return lifecycleScope.launch{
            movieDao?.searchBasicInfoRateAscending("%$value%")?.collect { list ->
                val mySearchList = ArrayList<BasicInfo>()
                for (item in list) mySearchList.add(item)
                setItemAdapter(mySearchList)
            }
        }
    }

    private fun setItemAdapter(list: ArrayList<BasicInfo>){
        if(list.isNullOrEmpty()){
            tvNothingFound.visibility = View.VISIBLE
            rvItemList.visibility = View.GONE
        }
        else {
            tvNothingFound.visibility = View.GONE
            rvItemList.visibility = View.VISIBLE

            rvItemList.layoutManager = GridLayoutManager(this, 3)
            itemAdapter = ItemAdapter(
                    category!!,
                    list, this@ItemListActivity
            )
            itemAdapter.setFabListener { bool ->
                fabRemove.isEnabled = bool
            }
            rvItemList.adapter = itemAdapter
        }
    }

    override fun onClick(view: View?) {
        when(view){
            btAddItem -> {
                var intent: Intent? = null
                when(category){
                    "MOVIE" -> { intent = Intent(this, MovieSearchActivity::class.java) }
                    "BOOK" -> { intent = Intent(this, MovieSearchActivity::class.java) }
                }
                intent?.putExtra("Category", category)
                startActivity(intent)
            }

            btSelect -> {
                if (Constants.selectMode) {
                    btSelect.text = "선택"
                    fabRemove.visibility = View.GONE
                }
                else {
                    btSelect.text = "취소"
                    fabRemove.visibility = View.VISIBLE
                }
                Constants.selectMode = !Constants.selectMode
            }

            fabRemove -> {
                val removeDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialog)
                removeDialog.setContentView(R.layout.remove_dialog)
                removeDialog.setCancelable(true)

                removeDialog.btRemoveCheck.setOnClickListener {
                    val selectedItem = itemAdapter.getSelectedItem()
                    lifecycleScope.launch {
                        when (category) {
                            "MOVIE" -> { movieDao?.deleteIdList(selectedItem) }
                            "BOOK" -> {

                            }
                        }
                    }
                    Constants.selectMode = false
                    btSelect.text = "선택"
                    fabRemove.visibility = View.GONE
                    removeDialog.dismiss()
                }
                removeDialog.btRemoveCancel.setOnClickListener {
                    removeDialog.dismiss()
                }

                removeDialog.show()
            }
            btSort, btSortImage -> {
                val sortDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialog)
                sortDialog.setContentView(R.layout.sort_dialog)
                sortDialog.setCancelable(true)

                sortDialog.btDateDescending.setOnClickListener {
                    sortNum = 1
                    fetchInfo(curString)
                    btSort.text = "최신순"
                    sortDialog.dismiss()
                }
                sortDialog.btDateAscending.setOnClickListener {
                    sortNum = 2
                    fetchInfo(curString)
                    btSort.text = "오래된순"
                    sortDialog.dismiss()
                }
                sortDialog.btRateDescending.setOnClickListener {
                    sortNum = 3
                    fetchInfo(curString)
                    btSort.text = "별점높은순"
                    sortDialog.dismiss()
                }
                sortDialog.btRateAscending.setOnClickListener {
                    sortNum = 4
                    fetchInfo(curString)
                    btSort.text = "별점낮은순"
                    sortDialog.dismiss()
                }

                sortDialog.show()
            }
        }
    }

}