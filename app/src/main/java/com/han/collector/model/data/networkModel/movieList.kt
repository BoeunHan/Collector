package com.han.collector.model.data.networkModel

import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("total") val total: Int,
    @SerializedName("items") val movieItems: ArrayList<MovieItem>
)