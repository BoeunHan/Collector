package com.example.collectors.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.collectors.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movieEntity: MovieEntity)

    @Update
    suspend fun update(movieEntity: MovieEntity)

    @Delete
    suspend fun delete(movieEntity: MovieEntity)

    @Query("SELECT * FROM `${Constants.TABLE_MOVIE_LIST}`")
    fun fetchAllMovies(): Flow<List<MovieEntity>>
}