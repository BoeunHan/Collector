package com.han.collector.model.data.remote.dataSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.han.collector.BuildConfig
import com.han.collector.model.data.remote.model.MovieItem
import com.han.collector.model.data.remote.api.SearchApiService
import com.han.collector.model.data.remote.model.PlaceItem


class PlaceSearchPagingSource(
    val service: SearchApiService,
    val query: String
) : PagingSource<Int, PlaceItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlaceItem> {
        return try {
            val key = params.key ?: 0
            val position = key*params.loadSize + 1

            val response = service.getPlaceSearchResult(
                BuildConfig.NAVER_API_ID,
                BuildConfig.NAVER_API_SECRET,
                query,
                position,
                params.loadSize
            )
            Log.e("response", "total size = ${response.total}, current size = ${response.placeItems.size}, current key = $key")
            LoadResult.Page(
                data = response.placeItems,
                prevKey = null,
                nextKey = if (response.total >= position + params.loadSize) key.plus(1) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PlaceItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}