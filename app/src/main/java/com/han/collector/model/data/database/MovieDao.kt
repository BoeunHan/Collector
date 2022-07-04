package com.han.collector.model.data.database

import androidx.paging.PagingSource
import androidx.room.*
import com.han.collector.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao : BaseDao<MovieEntity> {

    @Query("DELETE FROM `${Constants.TABLE_MOVIE_LIST}` WHERE id = :id")
    override suspend fun delete(id: Int)

    @Query("DELETE FROM `${Constants.TABLE_MOVIE_LIST}` WHERE id in (:idSet)")
    override suspend fun deleteIdSet(idSet: Set<Int>)

    @Query("SELECT * FROM `${Constants.TABLE_MOVIE_LIST}` WHERE id = :id")
    override fun fetchData(id: Int): Flow<MovieEntity>

    @Query("UPDATE `${Constants.TABLE_MOVIE_LIST}` SET `like` = :like WHERE id = :id")
    override suspend fun like(id: Int, like: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title = :title AND image = :image)")
    override suspend fun checkExist(title: String, image: String): Boolean

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` ORDER BY id DESC LIMIT 10")
    override fun fetchRecent(): PagingSource<Int, BasicInfo>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value ORDER BY id ASC")
    override fun fetchDateAscending(value: String): PagingSource<Int, BasicInfo>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value ORDER BY id DESC")
    override fun fetchDateDescending(value: String): PagingSource<Int, BasicInfo>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value ORDER BY rate ASC")
    override fun fetchRateAscending(value: String): PagingSource<Int, BasicInfo>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value ORDER BY rate DESC")
    override fun fetchRateDescending(value: String): PagingSource<Int, BasicInfo>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE `like`=1 ORDER BY id DESC")
    override fun fetchLike(): PagingSource<Int, BasicInfo>

}