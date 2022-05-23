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

    @Query("DELETE FROM `${Constants.TABLE_MOVIE_LIST}` WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM `${Constants.TABLE_MOVIE_LIST}`")
    fun fetchAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}`")
    fun fetchBasicInfo(): Flow<List<BasicInfo>>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value")
    fun fetchBasicInfoSearch(value: String): Flow<List<BasicInfo>>

    @Query("SELECT * FROM `${Constants.TABLE_MOVIE_LIST}` WHERE `like`=1")
    fun fetchLikeMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value")
    fun fetchSearchMovies(value: String): Flow<List<MovieEntity>>

    @Query("UPDATE `${Constants.TABLE_MOVIE_LIST}` SET `like` = :like WHERE id = :id")
    suspend fun likeMovie(id: Int, like: Boolean)

}