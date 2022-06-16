package com.example.collectors.model.data.networkModel

import android.text.Html
import com.google.gson.annotations.SerializedName

data class MovieItem(
    @SerializedName("title") val rawTitle: String,
    @SerializedName("image") val image: String,
    @SerializedName("pubDate") val pubDate: String,
    @SerializedName("director") val director: String,
    @SerializedName("actor") val actor: String
){
    val title: String
    get() = Html.fromHtml(this.rawTitle).toString()
}