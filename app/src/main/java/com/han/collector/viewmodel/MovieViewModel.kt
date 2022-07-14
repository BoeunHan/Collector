package com.han.collector.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.han.collector.model.data.database.MovieEntity
import com.han.collector.model.repository.MovieRepository
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
    var uploadDate: String = "",
    var like: Boolean = false
)

@HiltViewModel
class MovieViewModel @Inject constructor(
    val movieRepository: MovieRepository
) : ViewModel() {

    var movieStatus = MovieStatus()

    var rate = MutableStateFlow(0.0f)
    var summary = MutableStateFlow("")
    var review = MutableStateFlow("")

    val movieDetail = MutableStateFlow(MovieEntity())

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
            }
        }
    }

    fun setMovieStatus(title: String, image: String) {
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
            if (movieStatus.uploadDate == "") datestr else movieStatus.uploadDate,
            if (movieStatus.uploadDate == "") "" else datestr,
            movieStatus.like
        )

        viewModelScope.launch {
            movieRepository.insert(movie)
        }
    }
}