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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ItemListActivity : AppCompatActivity(), View.OnClickListener {

    private var movieDao: MovieDao? = null
    private var category: String? = null
    private lateinit var itemAdapter: ItemAdapter

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        movieDao = (application as CollectorApp).db.movieDao()
        category = intent.getStringExtra(Constants.CATEGORY)

        tvListCategory.text = category

        fetchInfo("")

        btCancel.setOnClickListener {
            etSearchItem.setText("")
        }

        lifecycleScope.launch{
            val editTextFlow = etSearchItem.textToFlow()
            editTextFlow
                .debounce(500)
                .onEach{ fetchInfo(it.toString()) }
                .launchIn(this)
        }

        Constants.selectMode = false
        btSelect.text = "선택"

        btAddItem.setOnClickListener(this)
        btSelect.setOnClickListener(this)
        fabRemove.setOnClickListener(this)
    }

    private fun fetchInfo(value: String){
        when(category){
            "MOVIE" -> {
                lifecycleScope.launch{
                    movieDao?.searchBasicInfo("%$value%")?.collect { list ->
                        val mySearchList = ArrayList<BasicInfo>()
                        for (item in list) mySearchList.add(item)
                        setItemAdapter(mySearchList)
                    }
                }
            }
            "BOOK" -> {
                setItemAdapter(ArrayList())
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
            itemAdapter.setFabListener { b ->
                fabRemove.isEnabled = b
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
        }
    }

}