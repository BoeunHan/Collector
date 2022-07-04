package com.han.collector.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.han.collector.model.data.database.BasicInfo
import com.han.collector.model.data.database.BookEntity
import com.han.collector.model.data.database.ReviewDatabase
import com.han.collector.viewmodel.SortField
import com.han.collector.viewmodel.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookRepository @Inject constructor(reviewDatabase: ReviewDatabase) {
    private val bookDao = reviewDatabase.bookDao()

    companion object {
        private val PAGE_SIZE = 15
        private val RECENT_PAGE_SIZE = 5
    }

    suspend fun insert(book: BookEntity) = bookDao.insert(book)

    suspend fun update(book: BookEntity) = bookDao.update(book)

    suspend fun delete(book: BookEntity) = bookDao.delete(book)

    suspend fun delete(id: Int) = bookDao.delete(id)

    suspend fun deleteIdSet(idSet: Set<Int>) = bookDao.deleteIdSet(idSet)

    fun fetchData(id: Int): Flow<BookEntity> = bookDao.fetchData(id)

    fun getRecentReviewFlow() = Pager(config = PagingConfig(pageSize = RECENT_PAGE_SIZE),
        pagingSourceFactory = { bookDao.fetchRecent() }).flow

    fun getReviewFlow(sort: Pair<SortField, SortType>, query: String): Flow<PagingData<BasicInfo>> {
        return when (sort) {
            Pair(SortField.DATE, SortType.DESCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { bookDao.fetchDateDescending(query) })
            }
            Pair(SortField.DATE, SortType.ASCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { bookDao.fetchDateAscending(query) })
            }
            Pair(SortField.RATE, SortType.DESCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { bookDao.fetchRateDescending(query) })
            }
            Pair(SortField.RATE, SortType.ASCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { bookDao.fetchRateAscending(query) })
            }
            Pair(SortField.LIKE, SortType.DESCENDING),
            Pair(SortField.LIKE, SortType.ASCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { bookDao.fetchLike() })
            }
            else -> null
        }?.flow ?: flow {}
    }

    suspend fun like(id: Int, like: Boolean) = bookDao.like(id, like)

    suspend fun checkExist(title: String, image: String): Boolean = bookDao.checkExist(title, image)

}