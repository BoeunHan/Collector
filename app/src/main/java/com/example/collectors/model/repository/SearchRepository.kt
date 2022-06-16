package com.example.collectors.model.repository

import android.content.Context
import com.example.collectors.utils.Constants
import com.example.collectors.model.data.networkModel.MovieList
import com.example.collectors.model.data.networkModel.BookList
import com.example.collectors.network.SearchApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import javax.inject.Inject

class SearchRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val searchApiService: SearchApiService,
) {

    fun getMovieApiCall(value: String): Call<MovieList>? {
        if (Constants.isNetworkAvailable(context)) {
            return searchApiService.getMovieSearchResult(
                Constants.NAVER_API_ID,
                Constants.NAVER_API_SECRET,
                value,
                30
            )
        }
        return null
    }
    fun getBookApiCall(value: String): Call<BookList>? {
        if (Constants.isNetworkAvailable(context)) {
            return searchApiService.getBookSearchResult(
                Constants.NAVER_API_ID,
                Constants.NAVER_API_SECRET,
                value,
                30
            )
        }
        return null
    }
}