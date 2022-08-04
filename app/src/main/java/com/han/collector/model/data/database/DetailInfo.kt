package com.han.collector.model.data.database

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailInfo(
    @ColumnInfo(name="id") val id: Int,
    @ColumnInfo(name="title") val title: String = "",
    @ColumnInfo(name="image") val image: Bitmap? = null,
    @ColumnInfo(name="rate") val rate: Float = 0.0f,
    @ColumnInfo(name="like") val like: Boolean,
    @ColumnInfo(name="uploadDate") val uploadDate: String = "",
    @ColumnInfo(name="editDate") val editDate: String = ""
) : Parcelable