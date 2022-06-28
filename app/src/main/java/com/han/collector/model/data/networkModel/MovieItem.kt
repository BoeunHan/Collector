package com.han.collector.model.data.networkModel

import android.text.Html
import com.google.gson.annotations.SerializedName

data class MovieItem(
    @SerializedName("title") val rawTitle: String,
    @SerializedName("link") val link: String,
    @SerializedName("image") val image: String,
    @SerializedName("pubDate") val pubDate: String,
    @SerializedName("director") val rawDirector: String,
    @SerializedName("actor") val rawActor: String
){
    val title: String
    get() = Html.fromHtml(this.rawTitle).toString()
    val director: String
    get() = if(rawDirector!="") rawDirector.substring(0, rawDirector.length - 1) else rawDirector
    val actor: String
    get() = if(rawActor!="") rawActor.substring(0, rawActor.length - 1) else rawActor
}