package com.han.collector.model.data.dataSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.han.collector.BuildConfig
import com.han.collector.model.data.networkModel.BookList
import com.han.collector.model.data.networkModel.MovieItem
import com.han.collector.model.data.networkModel.MovieList
import com.han.collector.network.SearchApiService
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class MovieSearchPagingSource(
    val service: SearchApiService,
    val query: String
) : PagingSource<Int, MovieItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return try{
            val position = params.key ?: 1
            val response = service.getMovieSearchResult(
                BuildConfig.NAVER_API_ID,
                BuildConfig.NAVER_API_SECRET,
                query,
                position,
                PAGE_SIZE
            )

            val prevKey = if (position > 1) position - PAGE_SIZE else null
            val nextKey = if (response.movieItems.size == PAGE_SIZE) position + PAGE_SIZE else null

            LoadResult.Page(
                data = response.movieItems,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(PAGE_SIZE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(PAGE_SIZE)
        }
    }

    companion object{
        const val PAGE_SIZE = 10
    }
}