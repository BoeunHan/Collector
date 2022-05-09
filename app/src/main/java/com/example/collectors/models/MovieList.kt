package com.example.collectors.models

import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("lastBuildDate") val lastBuildDate: String,
    @SerializedName("items") val items: ArrayList<Item>
)
