package com.example.collectors.database

import androidx.room.ColumnInfo

data class BasicInfo(
    @ColumnInfo(name="id") val id: Int,
    @ColumnInfo(name="title") val title: String = "",
    @ColumnInfo(name="image") val image: String = "",
    @ColumnInfo(name="rate") val rate: Float = 0.0f,
    @ColumnInfo(name="like") val like: Boolean
)
