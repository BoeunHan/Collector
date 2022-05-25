package com.example.collectors.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface BaseDao<T> {
    @Insert
    suspend fun insert(obj: T)

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)

    fun fetchAll(): Flow<List<T>>

    fun fetchAllBasicInfo(): Flow<List<BasicInfo>>

    fun searchBasicInfo(value: String): Flow<List<BasicInfo>>

    fun fetchLike(): Flow<List<T>>

    suspend fun like(id: Int, like: Boolean)
}