package com.example.collectors.database

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

    @Query("SELECT * FROM `${Constants.TABLE_MOVIE_LIST}` WHERE `like`=1")
    fun fetchLikeMovies(): Flow<List<MovieEntity>>

    @Query("UPDATE `${Constants.TABLE_MOVIE_LIST}` SET `like` = :like WHERE id = :id")
    suspend fun likeMovie(id: Int, like: Boolean)

}