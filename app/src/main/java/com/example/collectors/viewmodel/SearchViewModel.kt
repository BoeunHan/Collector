package com.example.collectors.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collectors.model.data.networkModel.BookItem
import com.example.collectors.model.data.networkModel.BookList
import com.example.collectors.model.data.networkModel.MovieItem
import com.example.collectors.model.data.networkModel.MovieList
import com.example.collectors.model.repository.BookRepository
import com.example.collectors.model.repository.MovieRepository
import com.example.collectors.model.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    init{
        getResult()
    }

    @FlowPreview
    fun getResult(){
        viewModelScope.launch{
            searchValue
                .debounce(500)
                .filter{ it.isNotEmpty() }
                .onEach{
                    when(category){
                        "MOVIE"->setMovieSearchResult(it)
                        "BOOK"->setBookSearchResult(it)
                    }
                }
                .launchIn(this)
        }
    }

    private fun setMovieSearchResult(value: String){
        searchRepository.getMovieApiCall(value)?.enqueue(object : Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    _movieSearchResult.update { result!!.movieItems }
                } else {
                    when (response.code()) {
                        400 -> Log.e("Error 400", "Bad connection.")
                        404 -> Log.e("Error 404", "Not found.")
                        else -> Log.e("Error", "Generic error.")
                    }
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Log.e("Error", "API call failed.")
            }
        })
    }

    private fun setBookSearchResult(value: String){
        searchRepository.getBookApiCall(value)?.enqueue(object : Callback<BookList> {
            override fun onResponse(call: Call<BookList>, response: Response<BookList>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    _bookSearchResult.update { result!!.bookItems }
                } else {
                    when (response.code()) {
                        400 -> Log.e("Error 400", "Bad connection.")
                        404 -> Log.e("Error 404", "Not found.")
                        else -> Log.e("Error", "Generic error.")
                    }
                }
            }

            override fun onFailure(call: Call<BookList>, t: Throwable) {
                Log.e("Error", "API call failed.")
            }
        })
    }

    fun clear(view: View){
        searchValue.update { "" }
    }

    suspend fun checkExist(title: String, image: String): Boolean{
        return when(category) {
            "MOVIE" -> movieRepository.checkExist(title, image)
            "BOOK" -> bookRepository.checkExist(title, image)
            else -> false
        }
    }
}