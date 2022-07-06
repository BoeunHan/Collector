package com.han.collector.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.han.collector.model.data.dataSource.BookSearchPagingSource
import com.han.collector.model.data.dataSource.MovieSearchPagingSource
import com.han.collector.network.SearchApiService
import javax.inject.Inject


class SearchRepository @Inject constructor(
    val searchApiService: SearchApiService
) {
    companion object {
        private val PAGE_SIZE = 10
    }

    fun getMovieSearchFlow(query: String) = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        ),
        initialKey = null,
        pagingSourceFactory = { MovieSearchPagingSource(searchApiService, query) }
    ).flow

    fun getBookSearchFlow(query: String) = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        ),
        initialKey = null,
        pagingSourceFactory = { BookSearchPagingSource(searchApiService, query) }
    ).flow
}