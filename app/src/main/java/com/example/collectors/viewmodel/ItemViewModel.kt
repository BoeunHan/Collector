package com.example.collectors.viewmodel

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collectors.R
import com.example.collectors.model.data.database.BasicInfo
import com.example.collectors.model.data.database.MovieEntity
import com.example.collectors.model.repository.CategoryRepository
import com.example.collectors.model.repository.MovieRepository
import com.example.collectors.view.adapters.ItemAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

enum class SortField{
    DATE, RATE
}
enum class SortType{
    ASCENDING, DESCENDING
}

@FlowPreview
@HiltViewModel
class ItemViewModel @Inject constructor(
    val categoryRepository: CategoryRepository,
    val movieRepository: MovieRepository
) : ViewModel() {
    private var _categoryList = MutableStateFlow(ArrayList<String>())
    val categoryList = _categoryList.asStateFlow()

    var searchValue = MutableStateFlow("")


    val movieList = movieRepository.fetchAllBasicInfo()


    private var sortFlow = MutableStateFlow(Triple(SortField.DATE, SortType.DESCENDING, searchValue.value))

    var sortModeName = MutableStateFlow("최신순")


    init {
        fetchCategoryList()
        getResult()
    }

    private fun fetchCategoryList() {
        _categoryList.update { categoryRepository.getCategory() }

    }

    fun setCategoryList(list: ArrayList<String>) {
        _categoryList.update { list }
        categoryRepository.setCategory(list)
    }


    private fun getResult() {
        viewModelScope.launch {
            searchValue
                .debounce(500)
                .onEach { value -> sortFlow.update { it.copy(third = value)  }
                    Log.e("searchvalue",value)}
                .launchIn(this)
        }
    }

    @ExperimentalCoroutinesApi
    val itemList = sortFlow.flatMapLatest{
        when(it.first){
            SortField.DATE -> {
                when(it.second){
                    SortType.DESCENDING -> {
                        movieRepository.searchBasicInfoDateDescending("%${it.third}%")
                    }
                    SortType.ASCENDING -> {
                        movieRepository.searchBasicInfoDateAscending("%${it.third}%")
                    }
                }
            }
            SortField.RATE -> {
                when(it.second){
                    SortType.DESCENDING -> {
                        movieRepository.searchBasicInfoRateDescending("%${it.third}%")
                    }
                    SortType.ASCENDING -> {
                        movieRepository.searchBasicInfoRateAscending("%${it.third}%")
                    }
                }
            }
        }
    }

    fun removeItem(category: String, id: Int){
        viewModelScope.launch {
            when (category) {
                "MOVIE" -> {
                    movieRepository.delete(id)
                }
                "BOOK" -> {

                }
            }
        }
    }

    fun setMode(view: View) {
        when (view.id) {
            R.id.btDateDescending -> {
                sortFlow.update{
                    it.copy(first = SortField.DATE, second = SortType.DESCENDING)
                }
                sortModeName.update { "최신순" }
            }
            R.id.btDateAscending -> {
                sortFlow.update{
                    it.copy(first = SortField.DATE, second = SortType.ASCENDING)
                }
                sortModeName.update { "오래된순" }
            }
            R.id.btRateDescending -> {
                sortFlow.update{
                    it.copy(first = SortField.RATE, second = SortType.DESCENDING)
                }
                sortModeName.update { "별점높은순" }
            }
            R.id.btRateAscending -> {
                sortFlow.update{
                    it.copy(first = SortField.RATE, second = SortType.ASCENDING)
                }
                sortModeName.update { "별점낮은순" }
            }
        }
    }

    fun clear() {
        searchValue.update { "" }
    }

    fun clear(view: View) {
        searchValue.update { "" }
    }

    fun getMovieDetail(id: Int) = movieRepository.fetchData(id)

    suspend fun setMovieLike(id: Int, like: Boolean) = movieRepository.like(id, like)
}
