package com.han.collector.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.han.collector.model.data.dataSource.BookSearchPagingSource
import com.han.collector.model.data.dataSource.MovieSearchPagingSource
import com.han.collector.model.repository.BookRepository
import com.han.collector.model.repository.MovieRepository
import com.han.collector.network.SearchApiService
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
    val searchApiService: SearchApiService
) : ViewModel() {

    val searchValue = MutableStateFlow("")
    private var searchValueFlow = MutableStateFlow("")

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
                searchValueFlow.emit(it)
            }
            .launchIn(viewModelScope)
    }


    @ExperimentalCoroutinesApi
    val searchFlow = searchValueFlow.flatMapLatest { value ->
        when (category) {
            "영화" -> Pager(
                config = PagingConfig(pageSize = 10, initialLoadSize = 10),
                initialKey = null,
                pagingSourceFactory = { MovieSearchPagingSource(searchApiService, value) }
            ).flow
                .cachedIn(viewModelScope)
            "책" -> Pager(
                config = PagingConfig(pageSize = 10, initialLoadSize = 10),
                initialKey = null,
                pagingSourceFactory = { BookSearchPagingSource(searchApiService, value) }
            ).flow
                .cachedIn(viewModelScope)
            else -> flow {}
        }
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