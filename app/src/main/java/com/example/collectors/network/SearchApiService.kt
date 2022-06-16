package com.example.collectors.network

import com.example.collectors.model.data.networkModel.BookList
import com.example.collectors.model.data.networkModel.MovieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface SearchApiService {
    @GET("search/movie.json")
    fun getMovieSearchResult(
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") secret: String,
        @Query("query") value: String,
        @Query("display") display: Int,
    ): Call<MovieList>

    @GET("search/book.json")
    fun getBookSearchResult(
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") secret: String,
        @Query("query") value: String,
        @Query("display") display: Int,
    ): Call<BookList>

}