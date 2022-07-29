package com.han.collector.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.han.collector.model.data.database.PlaceEntity
import com.han.collector.model.repository.FirestoreRepository
import com.han.collector.model.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


import javax.inject.Inject

data class PlaceStatus(
    var id: Int = 0,
    var title: String = "",
    var uploadDate: String = "",
    var like: Boolean = false
)

@HiltViewModel
class PlaceViewModel @Inject constructor(
    val placeRepository: PlaceRepository,
    val firestoreRepository: FirestoreRepository
) : ViewModel() {

    var placeStatus = PlaceStatus()

    var image = MutableStateFlow("")
    var rate = MutableStateFlow(0.0f)
    var goods = MutableStateFlow("")
    var bads = MutableStateFlow("")
    var memo = MutableStateFlow("")

    fun getPlaceDetail(id: Int) = placeRepository.fetchData(id)

    fun setPlaceStatus(id: Int) {
        viewModelScope.launch {
            placeRepository.fetchData(id).collectLatest { place ->
                placeStatus = placeStatus.copy(
                    id = place.id,
                    title = place.title,
                    uploadDate = place.uploadDate,
                    like = place.like
                )
                image.update { place.image }
                rate.update { place.rate }
                goods.update { place.goods }
                bads.update { place.bads }
                memo.update { place.memo }
            }
        }
    }

    fun setPlaceStatus(title: String) {
        placeStatus = placeStatus.copy(
            title = title
        )
    }

    fun savePlace() {
        val date = Date()
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val datestr = sdf.format(date)

        val place = PlaceEntity(
            placeStatus.id,
            placeStatus.title,
            image.value,
            rate.value,
            goods.value,
            bads.value,
            memo.value,
            if (placeStatus.uploadDate == "") datestr else placeStatus.uploadDate,
            if (placeStatus.uploadDate == "") "" else datestr,
            placeStatus.like
        )

        viewModelScope.launch (Dispatchers.IO){
            val id = placeRepository.insert(place)
            firestoreRepository.upload("장소", id.toInt(), "I")
        }
    }
}