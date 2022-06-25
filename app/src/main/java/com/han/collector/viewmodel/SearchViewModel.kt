package com.han.collector.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.han.collector.model.data.networkModel.BookItem
import com.han.collector.model.data.networkModel.BookList
import com.han.collector.model.data.networkModel.MovieItem
import com.han.collector.model.data.networkModel.MovieList
import com.han.collector.model.repository.BookRepository
import com.han.collector.model.repository.MovieRepository
import com.han.collector.model.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class SearchViewModel @Inject constructor(
    val searchRepository: SearchRepository,
    val movieRepository: MovieRepository,
    val bookRepository: BookRepository
) : ViewModel() {

    private val _movieSearchResult = MutableStateFlow(ArrayList<MovieItem>())
    val movieSearchResult = _movieSearchResult.asStateFlow()

    private val _bookSearchResult = MutableStateFlow(ArrayList<BookItem>())
    val bookSearchResult = _bookSearchResult.asStateFlow()

    val searchValue = MutableStateFlow("")

    var category: String = ""

    init {
        getResult()
    }

    @FlowPreview
    fun getResult() {
        searchValue
            .debounce(500)
            .filter { it.isNotEmpty() }
            .onEach {
                when (category) {
                    "영화" -> setMovieSearchResult(it)
                    "책" -> setBookSearchResult(it)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun setMovieSearchResult(value: String) {
        searchRepository.getMovieApiCall(value)?.enqueue(object : Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    _movieSearchResult.update { result!!.movieItems }
                } else {
                    when (response.code()) {
                        400 -> Log.e("Error 400", "Incorrect request.")
                        404 -> Log.e("Error 404", "Invalid search api.")
                        500 -> Log.e("Error 500", "System error.")
                    }
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Log.e("Error", "API call failed.")
            }
        })
    }

    private fun setBookSearchResult(value: String) {
        searchRepository.getBookApiCall(value)?.enqueue(object : Callback<BookList> {
            override fun onResponse(call: Call<BookList>, response: Response<BookList>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    _bookSearchResult.update { result!!.bookItems }
                } else {
                    when (response.code()) {
                        400 -> Log.e("Error 400", "Incorrect request.")
                        404 -> Log.e("Error 404", "Invalid search api.")
                        500 -> Log.e("Error 500", "System error.")
                    }
                }
            }

            override fun onFailure(call: Call<BookList>, t: Throwable) {
                Log.e("Error", "API call failed.")
            }
        })
    }

    fun clear(view: View) {
        searchValue.update { "" }
    }

    suspend fun checkExist(title: String, image: String): Boolean {
        return when (category) {
            "영화" -> movieRepository.checkExist(title, image)
            "책" -> bookRepository.checkExist(title, image)
            else -> false
        }
    }
}