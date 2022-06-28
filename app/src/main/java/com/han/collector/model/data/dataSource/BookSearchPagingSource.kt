package com.han.collector.model.data.dataSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.han.collector.BuildConfig
import com.han.collector.model.data.networkModel.BookItem
import com.han.collector.network.SearchApiService

class BookSearchPagingSource(
    val service: SearchApiService,
    val query: String
) : PagingSource<Int, BookItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookItem> {
        return try{
            val position = params.key ?: 1
            val response = service.getBookSearchResult(
                BuildConfig.NAVER_API_ID,
                BuildConfig.NAVER_API_SECRET,
                query,
                position,
                PAGE_SIZE
            )

            Log.e("response", response.bookItems.toString())

            val prevKey = if (position > 1) position - PAGE_SIZE else null
            val nextKey = if (response.bookItems.size == PAGE_SIZE) position + PAGE_SIZE else null

            LoadResult.Page(
                data = response.bookItems,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BookItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(PAGE_SIZE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(PAGE_SIZE)
        }
    }

    companion object{
        const val PAGE_SIZE = 10
    }
}