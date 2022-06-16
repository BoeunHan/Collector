package com.example.collectors.model.repository

import android.content.Context
import com.example.collectors.utils.Constants
import com.example.collectors.model.data.networkModel.MovieList
import com.example.collectors.network.MovieApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import javax.inject.Inject

class SearchRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val movieApiService: MovieApiService
) {

    fun getMovieApiCall(value: String): Call<MovieList>? {
        if (Constants.isNetworkAvailable(context)) {
            return movieApiService.getSearchResult(
                Constants.NAVER_API_ID,
                Constants.NAVER_API_SECRET,
                "movie.json",
                value,
                30
            )
        }
        return null
    }
}