package com.example.collectors.model.data.networkModel

import com.google.gson.annotations.SerializedName

data class BookItem(
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String,
    @SerializedName("pubdate") val pubDate: String,
    @SerializedName("author") val author: String,
    @SerializedName("publisher") val publisher: String
)