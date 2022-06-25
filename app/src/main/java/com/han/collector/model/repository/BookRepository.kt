package com.han.collector.model.repository

import com.han.collector.model.data.database.BasicInfo
import com.han.collector.model.data.database.BookEntity
import com.han.collector.model.data.database.ReviewDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepository @Inject constructor(reviewDatabase: ReviewDatabase) {
    private val bookDao = reviewDatabase.bookDao()


    suspend fun insert(book: BookEntity) = bookDao.insert(book)

    suspend fun update(book: BookEntity) = bookDao.update(book)

    suspend fun delete(book: BookEntity) = bookDao.delete(book)

    suspend fun delete(id: Int) = bookDao.delete(id)

    suspend fun deleteIdSet(idSet: Set<Int>) = bookDao.deleteIdSet(idSet)

    fun fetchData(id: Int): Flow<BookEntity> = bookDao.fetchData(id)

    fun fetchRecentBasicInfo(): Flow<List<BasicInfo>> = bookDao.fetchRecentBasicInfo()

    fun searchBasicInfoDateAscending(value: String): Flow<List<BasicInfo>> = bookDao.searchBasicInfoDateAscending(value)

    fun searchBasicInfoDateDescending(value: String): Flow<List<BasicInfo>> = bookDao.searchBasicInfoDateDescending(value)

    fun searchBasicInfoRateAscending(value: String): Flow<List<BasicInfo>> = bookDao.searchBasicInfoRateAscending(value)

    fun searchBasicInfoRateDescending(value: String): Flow<List<BasicInfo>> = bookDao.searchBasicInfoRateDescending(value)

    fun fetchLike(): Flow<List<BasicInfo>> = bookDao.fetchLike()

    suspend fun like(id: Int, like: Boolean) = bookDao.like(id, like)

    suspend fun checkExist(title: String, image: String): Boolean = bookDao.checkExist(title, image)

}