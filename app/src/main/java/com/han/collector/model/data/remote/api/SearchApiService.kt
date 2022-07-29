package com.han.collector.model.data.remote.api

import com.han.collector.model.data.remote.model.BookList
import com.han.collector.model.data.remote.model.MovieList
import com.han.collector.model.data.remote.model.PlaceList
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
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

    @GET("search/local.json")
    suspend fun getPlaceSearchResult(
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") secret: String,
        @Query("query") query: String,
        @Query("start") start: Int,
        @Query("display") display: Int
    ): PlaceList

}