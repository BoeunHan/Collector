package com.han.collector.model.data.dataSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.han.collector.BuildConfig
import com.han.collector.model.data.networkModel.MovieItem
import com.han.collector.network.SearchApiService


class MovieSearchPagingSource(
    val service: SearchApiService,
    val query: String
) : PagingSource<Int, MovieItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return try {
            val key = params.key ?: 1
            val position = if (key == 1) 1 else 1 + params.loadSize * (key + 1)

            val response = service.getMovieSearchResult(
                BuildConfig.NAVER_API_ID,
                BuildConfig.NAVER_API_SECRET,
                query,
                position,
                params.loadSize
            )

            LoadResult.Page(
                data = response.movieItems,
                prevKey = null,
                nextKey = if (response.total >= position + params.loadSize) key.plus(1) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}