package com.han.collector.model.data.remote.model

import com.google.gson.annotations.SerializedName

data class BookList(
    @SerializedName("total") val total: Int,
    @SerializedName("items") val bookItems: ArrayList<BookItem>
)