package com.example.collectors.model.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T)

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)

    suspend fun delete(id: Int)

    suspend fun deleteIdSet(idSet: Set<Int>)

    fun fetchData(id: Int): Flow<T>

    fun fetchRecentBasicInfo(): Flow<List<BasicInfo>>

    fun searchBasicInfoDateAscending(value: String): Flow<List<BasicInfo>>

    fun searchBasicInfoDateDescending(value: String): Flow<List<BasicInfo>>

    fun searchBasicInfoRateAscending(value: String): Flow<List<BasicInfo>>

    fun searchBasicInfoRateDescending(value: String): Flow<List<BasicInfo>>

    fun fetchLike(): Flow<List<T>>

    suspend fun like(id: Int, like: Boolean)

    suspend fun checkExist(title: String, image: String): Boolean
}