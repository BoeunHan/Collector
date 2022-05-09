package com.example.collectors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object Constants {
    const val TABLE_MOVIE_LIST = "movie-list"

    const val SEARCH_API_BASE_URL = "https://openapi.naver.com/v1/"
    const val NAVER_API_ID = "AHx25CUEHS8itsVYc_3V"
    const val NAVER_API_SECRET = "gdGsd5BfO1"

    const val MOVIE_IMAGE = "movie_image"
    const val MOVIE_TITLE = "movie_title"
    const val MOVIE_SUMMARY = "movie_summary"
    const val MOVIE_REVIEW = "movie_review"
    const val MOVIE_RATE = "movie_rate"
    const val SELECTED_MOVIE = "selected_movie"

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}