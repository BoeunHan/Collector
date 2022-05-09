package com.example.collectors.network

import com.example.collectors.models.MovieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApiService {
    @GET("search/{type}")
    fun getSearchResult(
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") secret: String,
        @Path("type") type: String,
        @Query("query") value: String,
        @Query("display") display: Int,
    ): Call<MovieList>

}