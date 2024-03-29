package com.han.collector.model.data.database

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T): Long

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)

    suspend fun delete(id: Int)

    suspend fun deleteIdSet(idSet: Set<Int>)

    fun fetchData(id: Int): Flow<T>

    suspend fun getData(id: Int): T

    fun fetchDetailInfo(id: Int): Flow<DetailInfo>

    fun fetchAll(): Flow<List<T>>

    fun getAll(): List<T>?

    suspend fun like(id: Int, like: Boolean)

    fun fetchRecent(): PagingSource<Int, BasicInfo>

    fun fetchDateAscending(value: String): PagingSource<Int, BasicInfo>

    fun fetchDateDescending(value: String): PagingSource<Int, BasicInfo>

    fun fetchRateAscending(value: String): PagingSource<Int, BasicInfo>

    fun fetchRateDescending(value: String): PagingSource<Int, BasicInfo>

    fun fetchLike(value: String): PagingSource<Int, BasicInfo>
}