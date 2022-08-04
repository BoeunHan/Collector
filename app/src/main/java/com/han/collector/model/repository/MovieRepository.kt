package com.han.collector.model.repository

import android.graphics.Bitmap
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.han.collector.model.data.database.BasicInfo
import com.han.collector.model.data.database.MovieEntity
import com.han.collector.model.data.database.ReviewDatabase
import com.han.collector.viewmodel.SortField
import com.han.collector.viewmodel.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepository @Inject constructor(reviewDatabase: ReviewDatabase) {
    private val movieDao = reviewDatabase.movieDao()

    companion object {
        private val PAGE_SIZE = 15
        private val RECENT_PAGE_SIZE = 5
    }

    suspend fun insert(movie: MovieEntity) = movieDao.insert(movie)

    suspend fun update(movie: MovieEntity) = movieDao.update(movie)

    suspend fun delete(movie: MovieEntity) = movieDao.delete(movie)

    suspend fun delete(id: Int) = movieDao.delete(id)

    suspend fun deleteIdSet(idSet: Set<Int>) = movieDao.deleteIdSet(idSet)

    fun fetchData(id: Int): Flow<MovieEntity> = movieDao.fetchData(id)

    fun fetchDetailInfo(id: Int) = movieDao.fetchDetailInfo(id)

    fun fetchAll(): Flow<List<MovieEntity>> = movieDao.fetchAll()

    fun getRecentReviewFlow() = Pager(config = PagingConfig(pageSize = RECENT_PAGE_SIZE),
        pagingSourceFactory = { movieDao.fetchRecent() }).flow

    fun getReviewFlow(sort: Pair<SortField, SortType>, query: String): Flow<PagingData<BasicInfo>> {
        return when (sort) {
            Pair(SortField.DATE, SortType.DESCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { movieDao.fetchDateDescending(query) })
            }
            Pair(SortField.DATE, SortType.ASCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { movieDao.fetchDateAscending(query) })
            }
            Pair(SortField.RATE, SortType.DESCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { movieDao.fetchRateDescending(query) })
            }
            Pair(SortField.RATE, SortType.ASCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { movieDao.fetchRateAscending(query) })
            }
            Pair(SortField.LIKE, SortType.DESCENDING),
            Pair(SortField.LIKE, SortType.ASCENDING) -> {
                Pager(config = PagingConfig(pageSize = PAGE_SIZE),
                    pagingSourceFactory = { movieDao.fetchLike(query) })
            }
            else -> null
        }?.flow ?: flow {}
    }

    suspend fun like(id: Int, like: Boolean) = movieDao.like(id, like)

    suspend fun checkExist(title: String, image: Bitmap?): Boolean =
        movieDao.checkExist(title, image)

}