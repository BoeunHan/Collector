package com.han.collector.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.han.collector.model.data.database.BookEntity
import com.han.collector.model.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


import javax.inject.Inject

data class BookStatus(
    var id: Int = 0,
    var title: String = "",
    var image: String = "",
    var uploadDate: String = "",
    var like: Boolean = false
)

@HiltViewModel
class BookViewModel @Inject constructor(
    val bookRepository: BookRepository
) : ViewModel() {

    var bookStatus = BookStatus()

    var rate = MutableStateFlow(0.0f)
    var summary = MutableStateFlow("")
    var review = MutableStateFlow("")

    val bookDetail = MutableStateFlow(BookEntity())

    fun getBookDetail(id: Int){
        viewModelScope.launch {
            bookRepository.fetchData(id).collectLatest { book ->
                bookDetail.update { book }
            }
        }
    }

    fun setBookStatus(id: Int) {
        viewModelScope.launch {
            bookRepository.fetchData(id).collectLatest { book ->
                bookStatus = bookStatus.copy(
                    id = book.id,
                    title = book.title,
                    image = book.image,
                    uploadDate = book.uploadDate,
                    like = book.like
                )
                rate.update { book.rate }
                summary.update { book.summary }
                review.update { book.review }
            }
        }
    }
    fun setBookStatus(title: String, image: String) {
        bookStatus = bookStatus.copy(
            title = title,
            image = image
        )
    }

    fun saveBook() {
        val date = Date()
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val datestr = sdf.format(date)

        val movie = BookEntity(
            bookStatus.id,
            bookStatus.title,
            bookStatus.image,
            rate.value,
            summary.value,
            review.value,
            if (bookStatus.uploadDate == "") datestr else bookStatus.uploadDate,
            if (bookStatus.uploadDate == "") "" else datestr,
            bookStatus.like
        )

        viewModelScope.launch {
            bookRepository.insert(movie)
        }
    }
}