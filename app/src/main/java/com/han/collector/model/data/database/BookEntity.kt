package com.han.collector.model.data.database

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Blob
import com.han.collector.utils.Constants
import kotlinx.parcelize.Parcelize

@Entity(tableName= Constants.TABLE_BOOK_LIST)
@Parcelize
data class BookEntity(
    @PrimaryKey(autoGenerate=true)
    val id: Int = 0,
    val title: String = "",
    val image: Bitmap? = null,
    val rate: Float = 0.0f,
    val summary: String = "",
    val review: String = "",
    val memo: String = "",
    val uploadDate: String = "",
    val editDate: String = "",
    val like: Boolean = false
) : Parcelable

class UploadBookEntity(
    val id: Int = 0,
    val title: String = "",
    val image: Blob? = null,
    val rate: Float = 0.0f,
    val summary: String = "",
    val review: String = "",
    val memo: String = "",
    val uploadDate: String = "",
    val editDate: String = "",
    val like: Boolean = false
)