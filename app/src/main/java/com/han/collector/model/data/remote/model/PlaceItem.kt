package com.han.collector.model.data.remote.model

import android.text.Html
import com.google.gson.annotations.SerializedName

data class PlaceItem(
    @SerializedName("title") val rawTitle: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("roadAddress") val roadAddress: String,
    @SerializedName("mapx") val mapx: Int,
    @SerializedName("mapy") val mapy: Int
){
    val title: String
    get() = Html.fromHtml(this.rawTitle).toString()
}