package com.han.collector.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.han.collector.model.data.database.BasicInfo
import com.han.collector.model.data.database.PlaceEntity
import com.han.collector.model.data.database.ReviewDatabase
import com.han.collector.viewmodel.SortField
import com.han.collector.viewmodel.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaceRepository @Inject constructor(reviewDatabase: ReviewDatabase) {
    private val placeDao = reviewDatabase.placeDao()

    companion object {
        private val PAGE_SIZE = 15
        private val RECENT_PAGE_SIZE = 5
    }

    suspend fun insert(place: PlaceEntity) = placeDao.insert(place)

    suspend fun update(place: PlaceEntity) = placeDao.update(place)

    suspend fun delete(place: PlaceEntity) = placeDao.delete(place)

    suspend fun delete(id: Int) = placeDao.delete(id)

    suspend fun deleteIdSet(idSet: Set<Int>) = placeDao.deleteIdSet(idSet)

    fun fetchData(id: Int): Flow<PlaceEntity> = placeDao.fetchData(id)

    fun fetchDetailInfo(id: Int) = placeDao.fetchDetailInfo(id)

    fun fetchAll(): Flow<List<PlaceEntity>> = placeDao.fetchAll()

    fun getRecentReviewFlow() = Pager(config = PagingConfig(pageSize = RECENT_PAGE_SIZE),
        pagingSourceFactory = { placeDao.fetchRecent() }).flow

    fun getReviewFlow(sort: Pair<SortField, SortType>, query: String): Flow<PagingData<BasicInfo>> {
        return when (sort) {
            Pair(SortField.DATE, SortType.DESCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { placeDao.fetchDateDescending(query) })
            }
            Pair(SortField.DATE, SortType.ASCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { placeDao.fetchDateAscending(query) })
            }
            Pair(SortField.RATE, SortType.DESCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { placeDao.fetchRateDescending(query) })
            }
            Pair(SortField.RATE, SortType.ASCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { placeDao.fetchRateAscending(query) })
            }
            Pair(SortField.LIKE, SortType.DESCENDING),
            Pair(SortField.LIKE, SortType.ASCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { placeDao.fetchLike(query) })
            }
            else -> null
        }?.flow ?: flow {}
    }

    suspend fun like(id: Int, like: Boolean) = placeDao.like(id, like)

    suspend fun checkExist(title: String, image: String): Boolean =
        placeDao.checkExist(title, image)

}