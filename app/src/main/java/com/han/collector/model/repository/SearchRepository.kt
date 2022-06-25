package com.han.collector.model.repository

import android.content.Context
import com.han.collector.BuildConfig
import com.han.collector.utils.Constants
import com.han.collector.model.data.networkModel.MovieList
import com.han.collector.model.data.networkModel.BookList
import com.han.collector.network.SearchApiService
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
                BuildConfig.NAVER_API_ID,
                BuildConfig.NAVER_API_SECRET,
                value,
                30
            )
        }
        return null
    }
    fun getBookApiCall(value: String): Call<BookList>? {
        if (Constants.isNetworkAvailable(context)) {
            return searchApiService.getBookSearchResult(
                BuildConfig.NAVER_API_ID,
                BuildConfig.NAVER_API_SECRET,
                value,
                30
            )
        }
        return null
    }
}