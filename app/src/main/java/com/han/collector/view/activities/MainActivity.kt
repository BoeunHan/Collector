package com.han.collector.view.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.han.collector.databinding.ActivityMainBinding
import com.han.collector.databinding.CategoryDialogBinding
import com.han.collector.model.data.database.BasicInfo
import com.han.collector.utils.Constants
import com.han.collector.view.adapters.CategoryAdapter
import com.han.collector.view.adapters.ItemAdapter
import com.han.collector.viewmodel.ItemViewModel
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

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activity = this
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

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
                    rvMain.adapter = CategoryAdapter(categoryList, this@MainActivity)
                    { category, recyclerview -> setCategoryList(category, recyclerview) }
                } else binding.isEmpty = true

            }
        }



    }

    private fun setCategoryList(category: String, recyclerview: RecyclerView) {
        val arrayList = ArrayList<BasicInfo>()
        lifecycleScope.launch {
            when (category) {
                "영화" -> {
                    viewModel.movieList.collect { list ->
                        arrayList.clear()
                        for (i in list) arrayList.add(i)
                        setAdapter(arrayList, recyclerview, category)
                    }
                }
                "책" -> {
                    viewModel.bookList.collect { list ->
                        arrayList.clear()
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
            list, category, this@MainActivity, viewModel
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
        if (dialogBinding.cbMovie.isChecked) categoryList.add("영화")
        if (dialogBinding.cbBook.isChecked) categoryList.add("책")

        firebaseAnalytics.logEvent("category_save"){
            param("category_list", categoryList.toString())
        }

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
            "영화" -> intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
            "책" -> intent = Intent(this@MainActivity, BookDetailActivity::class.java)
        }
        intent.putExtra(Constants.SELECTED_ID, id)
        startActivity(intent)
    }

}