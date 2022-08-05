package com.han.collector.model.data.remote.model

import com.google.gson.annotations.SerializedName

data class PlaceList(
    @SerializedName("total") val total: Int,
    @SerializedName("items") val placeItems: ArrayList<PlaceItem>
)