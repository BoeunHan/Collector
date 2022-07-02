package com.han.collector.network

import com.han.collector.model.data.networkModel.BookList
import com.han.collector.model.data.networkModel.MovieList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface SearchApiService {
    @GET("search/movie.json")
    suspend fun getMovieSearchResult(
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") secret: String,
        @Query("query") query: String,
        @Query("start") start: Int,
        @Query("display") display: Int
    ): MovieList

    @GET("search/book.json")
    suspend fun getBookSearchResult(
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") secret: String,
        @Query("query") query: String,
        @Query("start") start: Int,
        @Query("display") display: Int
    ): BookList

}