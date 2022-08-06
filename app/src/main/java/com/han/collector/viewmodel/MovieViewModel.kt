package com.han.collector.viewmodel


import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.han.collector.model.data.database.MovieEntity
import com.han.collector.model.repository.FirestoreRepository
import com.han.collector.model.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


import javax.inject.Inject

data class MovieStatus(
    var id: Int = 0,
    var title: String = "",
    var image: Bitmap? = null,
    var uploadDate: String = "",
    var like: Boolean = false
)

@HiltViewModel
class MovieViewModel @Inject constructor(
    val movieRepository: MovieRepository,
    val firestoreRepository: FirestoreRepository
) : ViewModel() {

    var movieStatus = MovieStatus()

    var rate = MutableStateFlow(0.0f)
    var summary = MutableStateFlow("")
    var review = MutableStateFlow("")
    var memo = MutableStateFlow("")

    fun getMovieDetail(id: Int) = movieRepository.fetchData(id)

    fun setMovieStatus(id: Int) {
        viewModelScope.launch {
            movieRepository.fetchData(id).collectLatest { movie ->
                movieStatus = movieStatus.copy(
                    id = movie.id,
                    title = movie.title,
                    image = movie.image,
                    uploadDate = movie.uploadDate,
                    like = movie.like
                )
                rate.update { movie.rate }
                summary.update { movie.summary }
                review.update { movie.review }
                memo.update { movie.memo }
            }
        }
    }

    fun setMovieStatus(title: String, image: Bitmap?) {
        movieStatus = movieStatus.copy(
            title = title,
            image = image
        )
    }

    fun saveMovie() {
        val date = Date()
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val datestr = sdf.format(date)

        val movie = MovieEntity(
            movieStatus.id,
            movieStatus.title,
            movieStatus.image,
            rate.value,
            summary.value,
            review.value,
            memo.value,
            if (movieStatus.uploadDate == "") datestr else movieStatus.uploadDate,
            if (movieStatus.uploadDate == "") "" else datestr,
            movieStatus.like
        )

        viewModelScope.launch (Dispatchers.IO){
            val id = movieRepository.insert(movie)
            firestoreRepository.update("영화", id.toInt(), "I")
        }
    }
}