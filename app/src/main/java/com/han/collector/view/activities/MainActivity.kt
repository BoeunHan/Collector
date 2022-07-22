package com.han.collector.view.activities

import android.app.Dialog
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import com.han.collector.viewmodel.FirestoreViewModel
import com.han.collector.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@FlowPreview
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    val viewModel: ItemViewModel by viewModels()
    val firestoreViewModel: FirestoreViewModel by viewModels()

    private lateinit var categoryDialog: Dialog
    private lateinit var dialogBinding: CategoryDialogBinding
    var categoryList = ArrayList<String>()

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    interface Callback {
        fun event()
    }

    private val getLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            currentUser = auth.currentUser
            currentUser?.let {
                Log.e("uid", currentUser!!.uid)
                firestoreViewModel.download()
                val callback = object : Callback {
                    override fun event() = viewModel.fetchCategoryList()
                }
                firestoreViewModel.downloadCategory(callback)
                setMenuVisibility(true)
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

        setMenuVisibility(false)
        currentUser?.let {
            setMenuVisibility(true)
            viewModel.setProfile(currentUser!!.displayName, currentUser!!.photoUrl.toString())
            binding.drawerLayout.close()
        }

        setRecyclerView()

        firestoreViewModel
    }

    override fun onResume() {
        super.onResume()
        registerNetworkCallback()
    }

    override fun onPause() {
        super.onPause()
        unregisterNetworkCallback()
    }

    var job: Job? = null

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            job = if (currentUser == null) null
            else {
                //Toast.makeText(this@MainActivity, "인터넷 연결됨 - 백업 재개", Toast.LENGTH_SHORT).show()
                firestoreViewModel.uploadState()
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            if (currentUser != null) Toast.makeText(this@MainActivity, "인터넷 연결 끊김 - 백업 중지", Toast.LENGTH_SHORT).show()
            job?.cancel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun registerNetworkCallback() {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    fun unregisterNetworkCallback() {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun gotoLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        getLoginResult.launch(intent)
    }

    fun openDrawer() {
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

    fun doLogout() {
        if (Constants.isNetworkAvailable(this@MainActivity)) {
            firestoreViewModel.uploadCategory(currentUser!!.uid)
            auth.signOut()
            currentUser = auth.currentUser
            firestoreViewModel.clearDBTables()
            viewModel.setProfile("로그인", "")
            viewModel.clearCategoryList()
            setMenuVisibility(false)
        } else {
            Toast.makeText(this@MainActivity, "인터넷 연결 끊김", Toast.LENGTH_SHORT).show()
        }
    }

    fun doUnlink() {
        MaterialAlertDialogBuilder(this)
            .setTitle("탈퇴")
            .setMessage("정말 탈퇴하시겠습니까?")
            .setNegativeButton("아니오") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("예") { dialog, _ ->
                dialog.dismiss()
                if (Constants.isNetworkAvailable(this@MainActivity)) {
                    firestoreViewModel.clearDBTables()
                    firestoreViewModel.clearUserData(currentUser!!.uid)
                    currentUser?.delete()
                    currentUser = auth.currentUser
                    viewModel.setProfile("로그인", "")
                    viewModel.clearCategoryList()
                    setMenuVisibility(false)
                } else {
                    Toast.makeText(this@MainActivity, "인터넷 연결 끊김", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    fun setMenuVisibility(boolean: Boolean) {
        binding.navView.menu.findItem(R.id.category_item).isVisible = boolean
        binding.navView.menu.findItem(R.id.logout_item).isVisible = boolean
        binding.navView.menu.findItem(R.id.unlink_item).isVisible = boolean
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.category_item -> showCategoryDialog()
            R.id.logout_item -> doLogout()
            R.id.unlink_item -> doUnlink()
        }
        binding.drawerLayout.close()
        return true
    }
}