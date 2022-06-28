package com.han.collector.viewmodel


import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.han.collector.R
import com.han.collector.model.repository.BookRepository
import com.han.collector.model.repository.CategoryRepository
import com.han.collector.model.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList

enum class SortField {
    DATE, RATE, NONE
}

enum class SortType {
    ASCENDING, DESCENDING
}

@FlowPreview
@HiltViewModel
class ItemViewModel @Inject constructor(
    val categoryRepository: CategoryRepository,
    val movieRepository: MovieRepository,
    val bookRepository: BookRepository
) : ViewModel() {
    private var _categoryList = MutableStateFlow(ArrayList<String>())
    val categoryList = _categoryList.asStateFlow()

    var category = ""
    var searchValue = MutableStateFlow("")

    val movieList = movieRepository.fetchRecentBasicInfo()
    val bookList = bookRepository.fetchRecentBasicInfo()

    private val _selectedIdSet = MutableStateFlow(HashSet<Int>())
    val selectedIdSet = _selectedIdSet.asStateFlow()

    private var _selectMode = MutableStateFlow(false)
    val selectMode = _selectMode.asStateFlow()

    private var sortFlow =
        MutableStateFlow(Triple(SortField.DATE, SortType.DESCENDING, searchValue.value))
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
        searchValue
            .debounce(500)
            .onEach { value -> sortFlow.update { it.copy(third = value) } }
            .launchIn(viewModelScope)
    }

    @ExperimentalCoroutinesApi
    val itemList = sortFlow.flatMapLatest {
        when (it.first) {
            SortField.DATE -> {
                when (it.second) {
                    SortType.DESCENDING -> {
                        when (category) {
                            "영화" -> movieRepository.searchBasicInfoDateDescending("%${it.third}%")
                            "책" -> bookRepository.searchBasicInfoDateDescending("%${it.third}%")
                            else -> flow{}
                        }
                    }
                    SortType.ASCENDING -> {
                        when (category) {
                            "영화" -> movieRepository.searchBasicInfoDateAscending("%${it.third}%")
                            "책" -> bookRepository.searchBasicInfoDateAscending("%${it.third}%")
                            else -> flow{}
                        }
                    }
                }
            }
            SortField.RATE -> {
                when (it.second) {
                    SortType.DESCENDING -> {
                        when (category) {
                            "영화" -> movieRepository.searchBasicInfoRateDescending("%${it.third}%")
                            "책" -> bookRepository.searchBasicInfoRateDescending("%${it.third}%")
                            else -> flow{}
                        }
                    }
                    SortType.ASCENDING -> {
                        when (category) {
                            "영화" -> movieRepository.searchBasicInfoRateAscending("%${it.third}%")
                            "책" -> bookRepository.searchBasicInfoRateAscending("%${it.third}%")
                            else -> flow{}
                        }
                    }
                }
            }
            SortField.NONE -> {
                when (category) {
                    "영화" -> movieRepository.fetchLike()
                    "책" -> bookRepository.fetchLike()
                    else -> flow{}
                }
            }

        }
    }

    fun setMode(view: View) {
        when (view.id) {
            R.id.btDateDescending -> {
                sortFlow.update {
                    it.copy(first = SortField.DATE, second = SortType.DESCENDING)
                }
                sortModeName.update { "최신순" }
            }
            R.id.btDateAscending -> {
                sortFlow.update {
                    it.copy(first = SortField.DATE, second = SortType.ASCENDING)
                }
                sortModeName.update { "오래된순" }
            }
            R.id.btRateDescending -> {
                sortFlow.update {
                    it.copy(first = SortField.RATE, second = SortType.DESCENDING)
                }
                sortModeName.update { "별점높은순" }
            }
            R.id.btRateAscending -> {
                sortFlow.update {
                    it.copy(first = SortField.RATE, second = SortType.ASCENDING)
                }
                sortModeName.update { "별점낮은순" }
            }
            R.id.btLikes -> {
                sortFlow.update {
                    it.copy(first = SortField.NONE)
                }
                sortModeName.update { "♥" }
            }
        }
    }

    fun clear() {
        searchValue.update { "" }
    }

    fun clear(view: View) {
        searchValue.update { "" }
    }


    fun setSelectMode(boolean: Boolean) {
        _selectMode.update { boolean }
        if (!boolean) {
            _selectedIdSet.update { HashSet() }
        }
    }

    fun reverseSelectMode(view: View) {
        setSelectMode(!_selectMode.value)
    }

    fun onClickItem(id: Int) {
        if (_selectedIdSet.value.contains(id))
            _selectedIdSet.update {
                val idSet = HashSet(it)
                idSet.remove(id)
                idSet
            }
        else
            _selectedIdSet.update {
                val idSet = HashSet(it)
                idSet.add(id)
                idSet
            }
    }

    fun removeIdSet() {
        viewModelScope.launch {
            when (category) {
                "영화" -> movieRepository.deleteIdSet(selectedIdSet.value)
                "책" -> bookRepository.deleteIdSet(selectedIdSet.value)
            }
        }
        setSelectMode(false)
    }

    fun getMovieDetail(id: Int) = movieRepository.fetchData(id)
    fun getBookDetail(id: Int) = bookRepository.fetchData(id)

    suspend fun setMovieLike(id: Int, like: Boolean) = movieRepository.like(id, like)
    suspend fun setBookLike(id: Int, like: Boolean) = bookRepository.like(id, like)
}
