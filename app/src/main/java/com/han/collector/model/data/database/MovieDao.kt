package com.han.collector.model.data.database

import android.graphics.Bitmap
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

    @Query("SELECT * FROM `${Constants.TABLE_MOVIE_LIST}` WHERE id = :id")
    override suspend fun getData(id: Int): MovieEntity

    @Query("SELECT id, title, image, rate, `like`, uploadDate, editDate FROM `${Constants.TABLE_MOVIE_LIST}` WHERE id = :id")
    override fun fetchDetailInfo(id: Int): Flow<DetailInfo>

    @Query("SELECT * FROM `${Constants.TABLE_MOVIE_LIST}`")
    override fun fetchAll(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM `${Constants.TABLE_MOVIE_LIST}`")
    override fun getAll(): List<MovieEntity>?

    @Query("UPDATE `${Constants.TABLE_MOVIE_LIST}` SET `like` = :like WHERE id = :id")
    override suspend fun like(id: Int, like: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title = :title AND image = :image)")
    suspend fun checkExist(title: String, image: Bitmap?): Boolean

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` ORDER BY id DESC LIMIT 10")
    override fun fetchRecent(): PagingSource<Int, BasicInfo>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value OR memo LIKE :value ORDER BY id ASC")
    override fun fetchDateAscending(value: String): PagingSource<Int, BasicInfo>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value OR memo LIKE :value ORDER BY id DESC")
    override fun fetchDateDescending(value: String): PagingSource<Int, BasicInfo>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value OR memo LIKE :value ORDER BY rate ASC")
    override fun fetchRateAscending(value: String): PagingSource<Int, BasicInfo>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value OR memo LIKE :value ORDER BY rate DESC")
    override fun fetchRateDescending(value: String): PagingSource<Int, BasicInfo>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE `like`=1 AND (title LIKE :value OR summary LIKE :value OR review LIKE :value OR memo LIKE :value) ORDER BY id DESC")
    override fun fetchLike(value: String): PagingSource<Int, BasicInfo>

}