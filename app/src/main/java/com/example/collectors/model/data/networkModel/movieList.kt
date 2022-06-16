package com.example.collectors.model.data.networkModel

import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("lastBuildDate") val lastBuildDate: String,
    @SerializedName("items") val movieItems: ArrayList<MovieItem>
)