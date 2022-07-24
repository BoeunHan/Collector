package com.han.collector.model.data.remote.model

import android.text.Html
import com.google.gson.annotations.SerializedName

data class BookItem(
    @SerializedName("title") val rawTitle: String,
    @SerializedName("link") val link: String,
    @SerializedName("image") val image: String,
    @SerializedName("pubdate") val pubDate: String,
    @SerializedName("author") val rawAuthor: String,
    @SerializedName("publisher") val publisher: String
){
    val title: String
    get() = Html.fromHtml(this.rawTitle).toString()
    val author: String
    get() = Html.fromHtml(this.rawAuthor).toString()
}