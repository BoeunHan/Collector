package com.han.collector.model.data.networkModel

import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("items") val movieItems: ArrayList<MovieItem>
)