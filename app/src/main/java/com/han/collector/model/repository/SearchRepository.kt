package com.han.collector.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.han.collector.model.data.dataSource.BookSearchPagingSource
import com.han.collector.model.data.dataSource.MovieSearchPagingSource
import com.han.collector.model.data.networkModel.BookItem
import com.han.collector.model.data.networkModel.MovieItem
import com.han.collector.network.SearchApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchRepository @Inject constructor(
    val searchApiService: SearchApiService
) {
    companion object {
        private val PAGE_SIZE = 10
    }

    fun getMovieSearchFlow(query: String): Flow<PagingData<MovieItem>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            initialKey = null,
            pagingSourceFactory = { MovieSearchPagingSource(searchApiService, query) }
        ).flow
    }

    fun getBookSearchFlow(query: String): Flow<PagingData<BookItem>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            initialKey = null,
            pagingSourceFactory = { BookSearchPagingSource(searchApiService, query) }
        ).flow
    }
}