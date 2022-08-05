package com.han.collector.viewmodel


import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.han.collector.R
import com.han.collector.model.data.database.DetailInfo
import com.han.collector.model.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.collections.ArrayList

enum class SortField {
    DATE, RATE, LIKE
}

enum class SortType {
    ASCENDING, DESCENDING
}

@FlowPreview
@HiltViewModel
class ItemViewModel @Inject constructor(
    val categoryRepository: CategoryRepository,
    val movieRepository: MovieRepository,
    val bookRepository: BookRepository,
    val placeRepository: PlaceRepository,
    val firestoreRepository: FirestoreRepository
) : ViewModel() {
    private var _categoryList = MutableStateFlow(ArrayList<String>())
    val categoryList = _categoryList.asStateFlow()

    var category = ""
    var searchValue = MutableStateFlow("")

    val movieList = movieRepository.getRecentReviewFlow()
    val bookList = bookRepository.getRecentReviewFlow()
    val placeList = placeRepository.getRecentReviewFlow()

    private val _selectedIdSet = MutableStateFlow(HashSet<Int>())
    val selectedIdSet = _selectedIdSet.asStateFlow()

    private var _selectMode = MutableStateFlow(false)
    val selectMode = _selectMode.asStateFlow()

    private var _sortFlow =
        MutableStateFlow(Triple(SortField.DATE, SortType.DESCENDING, searchValue.value))
    val sortFlow = _sortFlow.asStateFlow()

    var sortModeName = MutableStateFlow("최신순")

    private var _nickname = MutableStateFlow<String?>("로그인")
    val nickname = _nickname.asStateFlow()
    private var _thumbnail = MutableStateFlow<String?>("")
    val thumbnail = _thumbnail

    init {
        fetchCategoryList()
        getResult()
    }

    fun setProfile(nickname: String?, thumbnail: String?) {
        _nickname.update { nickname }
        _thumbnail.update { thumbnail }
    }

    fun fetchCategoryList() {
        _categoryList.update { categoryRepository.getCategory() }

    }

    fun setCategoryList(list: ArrayList<String>) {
        _categoryList.update { list }
        categoryRepository.setCategory(list)
    }

    fun clearCategoryList(){
        _categoryList.update { ArrayList() }
        categoryRepository.clearCategory()
    }

    private fun getResult() {
        searchValue
            .debounce(500)
            .onEach { value -> _sortFlow.update { it.copy(third = value) } }
            .launchIn(viewModelScope)
    }

    @ExperimentalCoroutinesApi
    val itemFlow = _sortFlow.flatMapLatest {
        when (category) {
            "영화" -> movieRepository.getReviewFlow(Pair(it.first, it.second), "%${it.third}%")
            "책" -> bookRepository.getReviewFlow(Pair(it.first, it.second), "%${it.third}%")
            "장소" -> placeRepository.getReviewFlow(Pair(it.first, it.second), "%${it.third}%")
            else -> flow {}
        }
    }

    fun setMode(view: View) {
        when (view.id) {
            R.id.btDateDescending -> {
                _sortFlow.update { it.copy(first = SortField.DATE, second = SortType.DESCENDING) }
                sortModeName.update { "최신순" }
            }
            R.id.btDateAscending -> {
                _sortFlow.update { it.copy(first = SortField.DATE, second = SortType.ASCENDING) }
                sortModeName.update { "오래된순" }
            }
            R.id.btRateDescending -> {
                _sortFlow.update { it.copy(first = SortField.RATE, second = SortType.DESCENDING) }
                sortModeName.update { "별점높은순" }
            }
            R.id.btRateAscending -> {
                _sortFlow.update { it.copy(first = SortField.RATE, second = SortType.ASCENDING) }
                sortModeName.update { "별점낮은순" }
            }
            R.id.btLikes -> {
                _sortFlow.update { it.copy(first = SortField.LIKE) }
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
        viewModelScope.launch(Dispatchers.IO) {
            when (category) {
                "영화" -> {
                    movieRepository.deleteIdSet(selectedIdSet.value)
                    for (id in selectedIdSet.value) firestoreRepository.update("영화", id, "R")
                }
                "책" -> {
                    bookRepository.deleteIdSet(selectedIdSet.value)
                    for (id in selectedIdSet.value) firestoreRepository.update("책", id, "R")
                }
                "장소" -> {
                    placeRepository.deleteIdSet(selectedIdSet.value)
                    for (id in selectedIdSet.value) firestoreRepository.update("장소", id, "R")
                }
            }
            setSelectMode(false)
        }
    }

    fun setLike(id: Int, like: Boolean) {
        viewModelScope.launch {
            when (category) {
                "영화" -> {
                    movieRepository.like(id, !like)
                    firestoreRepository.update("영화", id, "I")
                }
                "책" -> {
                    bookRepository.like(id, !like)
                    firestoreRepository.update("책", id, "I")
                }
                "장소" -> {
                    placeRepository.like(id, !like)
                    firestoreRepository.update("장소", id, "I")
                }
            }
        }
    }

    fun getDetailInfo(id: Int): Flow<DetailInfo> {
        return when (category) {
            "영화" -> movieRepository.fetchDetailInfo(id)
            "책" -> bookRepository.fetchDetailInfo(id)
            "장소" -> placeRepository.fetchDetailInfo(id)
            else -> flowOf()
        }
    }
}
