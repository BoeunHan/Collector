package com.example.collectors.viewmodel

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.RatingBar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collectors.R
import com.example.collectors.model.data.database.MovieEntity
import com.example.collectors.model.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


import javax.inject.Inject

data class MovieStatus(
    var id: Int = 0,
    var title: String = "",
    var image: String = "",
    var rate: Float = 0.0f,
    var summary: String = "",
    var review: String = "",
    var uploadDate: String = "",
    var editDate: String = "",
    var like: Boolean = false
)

@HiltViewModel
class MovieViewModel @Inject constructor(
    val movieRepository: MovieRepository
) : ViewModel() {

    private var _movieStatus: MutableStateFlow<MovieStatus?> = MutableStateFlow(null)
    val movieStatus = _movieStatus.asStateFlow()


    fun setMovieStatus(movieEntity: MovieEntity){
        _movieStatus.value = MovieStatus(
            movieEntity.id,
            movieEntity.title,
            movieEntity.image,
            movieEntity.rate,
            movieEntity.summary,
            movieEntity.review,
            movieEntity.uploadDate,
            movieEntity.editDate,
            movieEntity.like
        )
    }
    fun setMovieStatus(title: String, image: String){
        _movieStatus.value = MovieStatus(
            title = title,
            image = image
        )
    }


    fun onClickSave(){
        val date = Date()
        val sdf = SimpleDateFormat("dd MMM, yyyy")
        val datestr = sdf.format(date)

        val movie = MovieEntity(
            _movieStatus.value?.id!!,
            _movieStatus.value?.title!!,
            _movieStatus.value?.image!!,
            _movieStatus.value?.rate!!,
            _movieStatus.value?.summary!!,
            _movieStatus.value?.review!!,
            if(_movieStatus.value?.uploadDate=="") datestr else _movieStatus.value?.uploadDate!!,
            if(_movieStatus.value?.uploadDate=="") "" else datestr,
            _movieStatus.value?.like!!
        )

        viewModelScope.launch {
            movieRepository.insert(movie)
        }
        _movieStatus.value = null
    }

    fun onRatingChanged(ratingBar: RatingBar, rating: Float, fromUser: Boolean){
        _movieStatus.value?.rate = rating
    }

    inner class MyTextWatcher(private val editText: EditText) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            when(editText.id){
                R.id.etSummary -> {
                    _movieStatus.value?.summary = p0.toString()
                }
                R.id.etReview -> {
                    _movieStatus.value?.review = p0.toString()
                }
            }
        }
    }



}