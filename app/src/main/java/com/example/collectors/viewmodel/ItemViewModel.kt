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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

@FlowPreview
@HiltViewModel
class ItemViewModel @Inject constructor(
    val categoryRepository: CategoryRepository,
    val movieRepository: MovieRepository
) : ViewModel() {
    private var _categoryList = MutableStateFlow(ArrayList<String>())
    val categoryList = _categoryList.asStateFlow()

    private var _searchValue = MutableStateFlow("")
    val searchValue = _searchValue.asStateFlow()

    private var _movieList = MutableStateFlow(ArrayList<BasicInfo>())
    val movieList = _movieList.asStateFlow()

    private var _sortMode = MutableStateFlow(1)

    private var _category = ""
    val category = _category

    private var _selectedIdSet = MutableStateFlow(HashSet<Int>())
    val selectedIdSet = _selectedIdSet.asStateFlow()


    private var _mainMode = MutableStateFlow(true)
    val mainMode = _mainMode.asStateFlow()
    private var _selectMode = MutableStateFlow(false)
    val selectMode = _selectMode.asStateFlow()



    private var _movieDetail: MutableStateFlow<MovieEntity?> = MutableStateFlow(null)
    val movieDetail = _movieDetail.asStateFlow()

    private var fetchJob: Job? = null
    private var movieJob: Job? = null
    private var bookJob: Job? = null

    init{
        fetchCategoryList()
        setMainList()
        //getResult()
    }

    private fun fetchCategoryList(){
        _categoryList.update { categoryRepository.getCategory() }

    }
    fun setCategoryList(list: ArrayList<String>){
        _categoryList.update { list }
        categoryRepository.setCategory(list)
        setMainList()
    }
    fun setMainList() {
        for (i in _categoryList.value) {
                when (i) {
                    "MOVIE" -> {
                        movieJob?.cancel()
                        movieJob = viewModelScope.launch {
                            movieRepository.fetchAllBasicInfo()
                                .collect { _movieList.update { it }}
                        }
                    }
                }

        }
    }
    fun getResult() {
        viewModelScope.launch {
            _searchValue
                .debounce(500)
                .filter { it.isNotEmpty() }
                .onEach { setItemList(it) }
                .launchIn(this)
        }
    }

    private suspend fun setItemList(value: String){
        _sortMode.collect {
            fetchJob?.cancel()
            when (category) {
                "MOVIE" -> fetchJob = setMovieList(value, it)
                //"BOOK" ->
            }
        }
    }
    private fun setMovieList(value: String, sortMode: Int): Job{

        return viewModelScope.launch {
            when (sortMode) {
                1 -> {
                    movieRepository.searchBasicInfoDateDescending(value)
                        .collect { _movieList.value = it as ArrayList }
                }
                2 -> {
                    movieRepository.searchBasicInfoDateAscending(value)
                        .collect { _movieList.value = it as ArrayList }
                }
                3 -> {
                    movieRepository.searchBasicInfoRateDescending(value)
                        .collect { _movieList.value = it as ArrayList }
                }
                4 -> {
                    movieRepository.searchBasicInfoRateAscending(value)
                        .collect { _movieList.value = it as ArrayList }
                }
            }
        }

    }


    fun removeSelectedItems(){
        viewModelScope.launch{
            when (_category) {
                "MOVIE" -> {
                    movieRepository.deleteIdSet(_selectedIdSet.value)
                }
                "BOOK" -> {

                }
            }
        }
    }
    fun setMode(view: View){
        when (view.id) {
            R.id.btDateDescending -> {_sortMode.value = 1}
            R.id.btDateAscending -> {_sortMode.value = 2}
            R.id.btRateDescending -> {_sortMode.value = 3}
            R.id.btRateAscending -> {_sortMode.value = 4}
        }
    }
    fun setCategory(category: String){
        _category = category
    }
    inner class MyTextWatcher : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            _searchValue.value = p0.toString()
        }
    }

    fun clear(){
        _searchValue.value = ""
    }

    fun clear(view: View){
        _searchValue.value = ""
    }
    fun onClickItem(view: View, id: Int){
        if(_selectedIdSet.value.contains(id)) _selectedIdSet.value.remove(id)
        else _selectedIdSet.value.add(id)
    }
    fun setMainMode(boolean: Boolean){
        _mainMode.value = boolean
        if(boolean){
            _movieDetail.value = null
        }
    }
    fun setSelectMode(boolean: Boolean){
        _selectMode.value = boolean
        if(!boolean){
            _selectedIdSet.value.clear()
        }
    }

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            when (_category) {
                "MOVIE" -> {
                    movieRepository.fetchData(id).collect {
                        _movieDetail.value = it
                    }
                }
            }
        }
    }
    private fun setLike(id: Int, like: Boolean){
        viewModelScope.launch {
            when (_category) {
                "MOVIE" -> {
                    movieRepository.like(id, like)
                }
            }
        }
    }

    fun onClickLike(){
        if(movieDetail.value!!.like) setLike(movieDetail.value!!.id, false)
        else setLike(movieDetail.value!!.id, true)
    }


}