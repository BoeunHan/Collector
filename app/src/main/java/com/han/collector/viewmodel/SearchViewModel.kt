package com.han.collector.viewmodel

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.han.collector.model.repository.BookRepository
import com.han.collector.model.repository.MovieRepository
import com.han.collector.model.repository.PlaceRepository
import com.han.collector.model.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class SearchViewModel @Inject constructor(
    val movieRepository: MovieRepository,
    val bookRepository: BookRepository,
    val placeRepository: PlaceRepository,
    val searchRepository: SearchRepository
) : ViewModel() {

    val searchValue = MutableStateFlow("")
    private val _searchValueFlow = MutableStateFlow("")
    val searchValueFlow = _searchValueFlow.asStateFlow()

    var category: String = ""


    init {
        getResult()
    }

    @FlowPreview
    fun getResult() {
        searchValue
            .debounce(500)
            .filter { it.isNotEmpty() }
            .onEach { value -> _searchValueFlow.update { value } }
            .launchIn(viewModelScope)
    }

    @ExperimentalCoroutinesApi
    val searchFlow = _searchValueFlow.flatMapLatest {
        when (category) {
            "영화" -> searchRepository.getMovieSearchFlow(it)
                .cachedIn(viewModelScope)
            "책" -> searchRepository.getBookSearchFlow(it)
                .cachedIn(viewModelScope)
            "장소" -> searchRepository.getPlaceSearchFlow(it)
                .cachedIn(viewModelScope)
            else -> flow {}
        }
    }


    fun clear(view: View) {
        searchValue.update { "" }
    }

    suspend fun checkExist(title: String, image: Bitmap?): Boolean {
        return when (category) {
            "영화" -> movieRepository.checkExist(title, image)
            "책" -> bookRepository.checkExist(title, image)
            "장소" -> placeRepository.checkExist(title)
            else -> false
        }
    }
}