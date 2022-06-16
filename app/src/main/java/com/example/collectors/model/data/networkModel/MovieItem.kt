package com.example.collectors.model.data.networkModel

import com.google.gson.annotations.SerializedName

data class MovieItem(
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String,
    @SerializedName("pubDate") val pubDate: String,
    @SerializedName("director") val director: String,
    @SerializedName("actor") val actor: String
)