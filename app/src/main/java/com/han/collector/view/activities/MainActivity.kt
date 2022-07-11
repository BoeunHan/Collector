package com.han.collector.view.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.han.collector.databinding.ActivityMainBinding
import com.han.collector.databinding.CategoryDialogBinding
import com.han.collector.utils.Constants
import com.han.collector.view.adapters.CategoryAdapter
import com.han.collector.view.adapters.ItemAdapter
import com.han.collector.viewmodel.ItemViewModel
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.AuthCodeHandlerActivity
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel: ItemViewModel by viewModels()

    private lateinit var categoryDialog: Dialog
    private lateinit var dialogBinding: CategoryDialogBinding
    var categoryList = ArrayList<String>()

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activity = this
        binding.lifecycleOwner = this

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)


        checkLoggedIn()

        setRecyclerView()

    }

    private fun checkLoggedIn() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { token, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        //기타 에러
                    }
                }
                else if (token != null){
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e("사용자 정보 요청 실패","")
                            val intent = Intent(this, AuthCodeHandlerActivity::class.java)
                            startActivity(intent)
                        } else if (user != null) {
                            Log.i(
                                "사용자 정보 요청 성공",
                                "${user.kakaoAccount?.profile?.nickname}, ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                            )
                        }
                    }
                }
            }
        }
        else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setRecyclerView() {
        val rvMain = binding.rvMain
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvMain.layoutManager = layoutManager

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryList.collectLatest { list ->
                    val arrayList = ArrayList(list)
                    categoryList = arrayList

                    if (categoryList.isNotEmpty()) {
                        binding.isEmpty = false
                        rvMain.adapter = CategoryAdapter(categoryList, this@MainActivity)
                    } else binding.isEmpty = true

                }
            }
        }
    }

    fun getCategoryList(category: String, pagingAdapter: ItemAdapter) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (category) {
                    "영화" -> viewModel.movieList
                    "책" -> viewModel.bookList
                    else -> flow {}
                }.collectLatest {
                    pagingAdapter.submitData(it)
                }
            }
        }
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

        firebaseAnalytics.logEvent("category_save") {
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
        val intent = Intent(this@MainActivity, ReviewDetailActivity::class.java)
        intent.putExtra(Constants.CATEGORY, category)
        intent.putExtra(Constants.SELECTED_ID, id)
        startActivity(intent)
    }

}