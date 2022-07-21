package com.han.collector.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.han.collector.model.repository.FirestoreRepository
import com.han.collector.view.activities.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class FirestoreViewModel @Inject constructor(
    val firestoreRepository: FirestoreRepository
) : ViewModel() {

    fun uploadState() = viewModelScope.launch(Dispatchers.IO) {
        firestoreRepository.uploadState()
    }

    fun uploadAll() = viewModelScope.async(Dispatchers.IO) {
        firestoreRepository.uploadAll()
    }

    fun download() = viewModelScope.launch(Dispatchers.IO){
        firestoreRepository.download(viewModelScope)
    }

    fun clearDBTables() = viewModelScope.launch(Dispatchers.IO) {
        firestoreRepository.clearDBTables()
    }

    fun clearUserData(uid: String) = viewModelScope.launch(Dispatchers.IO) {
        firestoreRepository.clearUserData(uid)
    }

    fun uploadCategory(uid: String) = firestoreRepository.uploadCategory(uid)

    fun downloadCategory(callback: MainActivity.Callback) = firestoreRepository.downloadCategory(callback)

}
