package com.han.collector.model.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.han.collector.utils.Constants
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