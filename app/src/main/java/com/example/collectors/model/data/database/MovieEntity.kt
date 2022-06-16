package com.example.collectors.model.data.database

import android.os.Parcelable
import android.text.Html
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.collectors.utils.Constants
import kotlinx.parcelize.Parcelize

@Entity(tableName= Constants.TABLE_MOVIE_LIST)
@Parcelize
data class MovieEntity(
    @PrimaryKey(autoGenerate=true)
    val id: Int = 0,
    val title: String = "",
    val image: String = "",
    val rate: Float = 0.0f,
    val summary: String = "",
    val review: String = "",
    val uploadDate: String = "",
    val editDate: String = "",
    val like: Boolean = false
) : Parcelable