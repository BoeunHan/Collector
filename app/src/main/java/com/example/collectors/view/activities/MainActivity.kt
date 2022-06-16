package com.example.collectors.view.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collectors.databinding.ActivityMainBinding
import com.example.collectors.databinding.CategoryDialogBinding
import com.example.collectors.model.data.database.BasicInfo
import com.example.collectors.utils.Constants
import com.example.collectors.view.adapters.CategoryAdapter
import com.example.collectors.view.adapters.ItemAdapter
import com.example.collectors.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ItemViewModel by viewModels()

    private lateinit var categoryDialog: Dialog
    private lateinit var dialogBinding: CategoryDialogBinding
    var categoryList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.activity = this
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this


        val rvMain = binding.rvMain
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvMain.layoutManager = layoutManager

        lifecycleScope.launch {
            viewModel.categoryList.collectLatest { list ->
                val arrayList = ArrayList<String>()
                for (i in list) arrayList.add(i)
                categoryList = arrayList

                if (categoryList.isNotEmpty()) {
                    binding.isEmpty = false
                    rvMain.adapter = CategoryAdapter(arrayList, this@MainActivity)
                    { category, recyclerview -> setCategoryList(category, recyclerview) }
                } else binding.isEmpty = true

            }
        }

        setContentView(binding.root)
    }

    private fun setCategoryList(category: String, recyclerview: RecyclerView) {
        lifecycleScope.launch {
            when (category) {
                "MOVIE" -> {
                    viewModel.movieList.collect { list ->
                        val arrayList = ArrayList<BasicInfo>()
                        for (i in list) arrayList.add(i)
                        setAdapter(arrayList, recyclerview, category)
                    }
                }
                "BOOK" -> {
                    viewModel.bookList.collect { list ->
                        val arrayList = ArrayList<BasicInfo>()
                        for (i in list) arrayList.add(i)
                        setAdapter(arrayList, recyclerview, category)
                    }
                }
            }
        }
    }

    private fun setAdapter(
        list: ArrayList<BasicInfo>,
        recyclerview: RecyclerView,
        category: String
    ) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerview.layoutManager = layoutManager

        recyclerview.adapter = ItemAdapter(
            list, category, this@MainActivity
        )
    }

    fun showCategoryDialog() {
        categoryDialog = Dialog(this)
        categoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogBinding = CategoryDialogBinding.inflate(layoutInflater)
        dialogBinding.activity = this
        categoryDialog.setContentView(dialogBinding.root)

        categoryDialog.show()
    }


    fun onClickCategorySave(view: View) {
        categoryList.clear()
        if (dialogBinding.cbMovie.isChecked) categoryList.add("MOVIE")
        if (dialogBinding.cbBook.isChecked) categoryList.add("BOOK")

        viewModel.setCategoryList(categoryList)
        categoryDialog.dismiss()
    }

    fun containsCategory(category: String): Boolean {
        return categoryList.contains(category)
    }

    fun onClickAdd(category: String) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(Constants.CATEGORY, category)
        startActivity(intent)
    }

    fun onClickMore(category: String) {
        val intent = Intent(this, ItemListActivity::class.java)
        intent.putExtra(Constants.CATEGORY, category)
        startActivity(intent)
    }

    fun getItemDetail(category: String, id: Int) {
        lateinit var intent: Intent
        when (category) {
            "MOVIE" -> intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
            "BOOK" -> intent = Intent(this@MainActivity, BookDetailActivity::class.java)
        }
        intent.putExtra(Constants.SELECTED_ID, id)
        startActivity(intent)
    }

}