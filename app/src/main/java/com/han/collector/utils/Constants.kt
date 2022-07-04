package com.han.collector.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.recyclerview.widget.DiffUtil
import com.han.collector.model.data.database.BasicInfo
import com.han.collector.model.data.networkModel.MovieItem

object Constants {
    const val TABLE_MOVIE_LIST = "movie-list"
    const val TABLE_BOOK_LIST = "book-list"

    const val SEARCH_API_BASE_URL = "https://openapi.naver.com/v1/"

    const val IMAGE = "image"
    const val TITLE = "title"
    const val CATEGORY = "category"
    const val SELECTED_ID = "selected_id"
    const val SELECTED_ITEM = "selected_item"
    const val SELECTED_MOVIE = "selected_movie"
    const val SELECTED_BOOK = "selected_book"

    const val CATEGORY_PREF = "category_pref"
    const val CATEGORY_DATA = "category_data"

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

    val REVIEW_COMPARATOR = object : DiffUtil.ItemCallback<BasicInfo>() {
        override fun areItemsTheSame(oldItem: BasicInfo, newItem: BasicInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BasicInfo, newItem: BasicInfo): Boolean {
            return oldItem == newItem
        }

    }
}