package com.example.collectors.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.collectors.Constants

@Entity(tableName= Constants.TABLE_MOVIE_LIST)
data class MovieEntity(
    @PrimaryKey(autoGenerate=true)
    val id: Int = 0,
    val title: String = "",
    val image: String = "",
    val rate: Float = 0.0f,
    val summary: String = "",
    val review: String = "",
    var uploadDate: String = "",
    val reviseDate: String = ""
)
