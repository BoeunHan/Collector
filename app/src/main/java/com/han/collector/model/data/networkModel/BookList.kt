package com.han.collector.model.data.networkModel

import com.google.gson.annotations.SerializedName

data class BookList(
    @SerializedName("items") val bookItems: ArrayList<BookItem>
)