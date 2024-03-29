package com.han.collector.model.data.remote.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.han.collector.BuildConfig
import com.han.collector.model.data.remote.model.MovieItem
import com.han.collector.model.data.remote.api.SearchApiService


class MovieSearchPagingSource(
    val service: SearchApiService,
    val query: String
) : PagingSource<Int, MovieItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return try {
            val key = params.key ?: 0
            val position = key*params.loadSize + 1

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