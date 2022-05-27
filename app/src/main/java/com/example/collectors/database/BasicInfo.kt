package com.example.collectors.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BasicInfo(
    @ColumnInfo(name="id") val id: Int,
    @ColumnInfo(name="title") val title: String = "",
    @ColumnInfo(name="image") val image: String = "",
    @ColumnInfo(name="rate") val rate: Float = 0.0f,
    @ColumnInfo(name="like") val like: Boolean
) : Parcelable
