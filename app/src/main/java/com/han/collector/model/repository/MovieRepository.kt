package com.han.collector.model.repository

import com.han.collector.model.data.database.BasicInfo
import com.han.collector.model.data.database.MovieEntity
import com.han.collector.model.data.database.ReviewDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor(reviewDatabase: ReviewDatabase) {
    private val movieDao = reviewDatabase.movieDao()


    suspend fun insert(movie: MovieEntity) = movieDao.insert(movie)

    suspend fun update(movie: MovieEntity) = movieDao.update(movie)

    suspend fun delete(movie: MovieEntity) = movieDao.delete(movie)

    suspend fun delete(id: Int) = movieDao.delete(id)

    suspend fun deleteIdSet(idSet: Set<Int>) = movieDao.deleteIdSet(idSet)

    fun fetchData(id: Int): Flow<MovieEntity> = movieDao.fetchData(id)

    fun fetchRecentBasicInfo(): Flow<List<BasicInfo>> = movieDao.fetchRecentBasicInfo()

    fun searchBasicInfoDateAscending(value: String): Flow<List<BasicInfo>> = movieDao.searchBasicInfoDateAscending(value)

    fun searchBasicInfoDateDescending(value: String): Flow<List<BasicInfo>> = movieDao.searchBasicInfoDateDescending(value)

    fun searchBasicInfoRateAscending(value: String): Flow<List<BasicInfo>> = movieDao.searchBasicInfoRateAscending(value)

    fun searchBasicInfoRateDescending(value: String): Flow<List<BasicInfo>> = movieDao.searchBasicInfoRateDescending(value)

    fun fetchLike(): Flow<List<BasicInfo>> = movieDao.fetchLike()

    suspend fun like(id: Int, like: Boolean) = movieDao.like(id, like)

    suspend fun checkExist(title: String, image: String): Boolean = movieDao.checkExist(title, image)

}