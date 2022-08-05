package com.han.collector.view.activities

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.han.collector.utils.Constants
import com.han.collector.databinding.ActivityAddReviewBinding
import com.han.collector.utils.Converters
import com.han.collector.view.fragments.AddBookFragment
import com.han.collector.view.fragments.AddMovieFragment
import com.han.collector.view.fragments.AddPlaceFragment
import com.han.collector.viewmodel.BookViewModel
import com.han.collector.viewmodel.MovieViewModel
import com.han.collector.viewmodel.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class AddReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReviewBinding
    private val movieViewModel: MovieViewModel by viewModels()
    private val bookViewModel: BookViewModel by viewModels()
    private val placeViewModel: PlaceViewModel by viewModels()

    private var itemImage: Bitmap? = null
    private var itemTitle: String? = null

    private var id: Int? = null
    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.activity = this

        val imageByteArray = intent.getByteArrayExtra(Constants.IMAGE)
        itemImage = imageByteArray?.let { Converters().toBitmap(it) }
        itemTitle = intent.getStringExtra(Constants.TITLE)
        category = intent.getStringExtra(Constants.CATEGORY)

        id = intent.getIntExtra(Constants.SELECTED_ID, 0)

        binding.title = itemTitle

        if (savedInstanceState == null) {
            when (category) {
                "영화" -> supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<AddMovieFragment>(binding.addFragmentContainerView.id)
                }
                "책" -> supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<AddBookFragment>(binding.addFragmentContainerView.id)
                }
                "장소" -> supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<AddPlaceFragment>(binding.addFragmentContainerView.id)
                }
            }
        }

        setData()
    }

    private fun setData() {
        when (category) {
            "영화" -> {
                if (id == 0) movieViewModel.setMovieStatus(itemTitle!!, itemImage)
                else movieViewModel.setMovieStatus(id!!)
            }
            "책" -> {
                if (id == 0) bookViewModel.setBookStatus(itemTitle!!, itemImage)
                else bookViewModel.setBookStatus(id!!)
            }
            "장소" -> {
                if (id == 0) placeViewModel.setPlaceStatus(itemTitle!!)
                else placeViewModel.setPlaceStatus(id!!)
            }
        }
    }

    fun saveData() {
        when (category) {
            "영화" -> movieViewModel.saveMovie()
            "책" -> bookViewModel.saveBook()
            "장소" -> placeViewModel.savePlace()
        }
        finish()
    }
}