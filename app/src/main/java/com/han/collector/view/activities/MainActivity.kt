package com.han.collector.view.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.han.collector.R
import com.han.collector.databinding.ActivityMainBinding
import com.han.collector.databinding.CategoryDialogBinding
import com.han.collector.databinding.HeaderNavigationDrawerBinding
import com.han.collector.utils.Constants
import com.han.collector.view.adapters.CategoryAdapter
import com.han.collector.view.adapters.ItemAdapter
import com.han.collector.view.fragments.ReviewDetailDialogFragment
import com.han.collector.viewmodel.ItemViewModel
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityMainBinding
    val viewModel: ItemViewModel by viewModels()

    private lateinit var categoryDialog: Dialog
    private lateinit var dialogBinding: CategoryDialogBinding
    var categoryList = ArrayList<String>()

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    private val getLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        currentUser = auth.currentUser
        currentUser?.let{
            viewModel.setProfile(currentUser!!.displayName, currentUser!!.photoUrl.toString())
            binding.drawerLayout.close()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.navView.setNavigationItemSelectedListener(this)

        val headerbinding = HeaderNavigationDrawerBinding.bind(binding.navView.getHeaderView(0))
        headerbinding.activity = this
        headerbinding.viewModel = viewModel
        headerbinding.lifecycleOwner = this

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        auth = Firebase.auth
        currentUser = auth.currentUser
        currentUser?.let{
            viewModel.setProfile(currentUser!!.displayName, currentUser!!.photoUrl.toString())
            binding.drawerLayout.close()
        }
        //checkLoggedIn()

        setRecyclerView()

    }


    fun gotoLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        getLoginResult.launch(intent)
    }


    /*private fun checkLoggedIn() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { token, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        doLogin()
                    }
                    else {
                        Log.e("Error",error.toString())
                    }
                }
                else if (token != null){
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e("사용자 정보 요청 실패","")
                            doLogin()
                        } else if (user != null) {
                            Log.i("사용자 정보 요청 성공",user.kakaoAccount?.profile?.nickname!!)
                            Toast.makeText(this@MainActivity, "로그인되었습니다.", Toast.LENGTH_SHORT).show()
                            binding.drawerLayout.close()
                            viewModel.setProfile(user.kakaoAccount?.profile?.nickname, user.kakaoAccount?.profile?.thumbnailImageUrl)
                        }
                    }
                }
            }
        }
        else {
            doLogin()
        }
    }
*/
    fun openDrawer(){
        binding.drawerLayout.open()
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
        viewModel.category = category
        val cardDialog = ReviewDetailDialogFragment()
        val bundle = Bundle()
        bundle.putString(Constants.CATEGORY, category)
        bundle.putInt(Constants.SELECTED_ID, id)
        cardDialog.arguments = bundle
        cardDialog.show(supportFragmentManager, ReviewDetailDialogFragment.TAG)
    }

    fun doLogout(){
        auth.signOut()
        viewModel.setProfile("로그인","")
    }
    fun doUnlink(){
        MaterialAlertDialogBuilder(this)
            .setTitle("탈퇴")
            .setMessage("정말 탈퇴하시겠습니까?")
            .setNegativeButton("아니오"){ dialog,_ ->
                dialog.dismiss()
            }
            .setPositiveButton("예"){dialog,_ ->
                dialog.dismiss()
                currentUser?.delete()
                viewModel.setProfile("로그인","")
            }
            .show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.category_item -> showCategoryDialog()
            R.id.logout_item -> doLogout()
            R.id.unlink_item -> doUnlink()
        }
        binding.drawerLayout.close()
        return true
    }

    val callback: (Throwable?) -> Unit = { error ->
        if(error != null){
            Log.e("사용자 정보 저장 실패", error.toString())
        } else {
            Log.i("사용자 정보 저장 성공", "")
        }
    }
}