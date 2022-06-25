package com.han.collector.model.data.database

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

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` ORDER BY id DESC LIMIT 10")
    override fun fetchRecentBasicInfo(): Flow<List<BasicInfo>>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value ORDER BY id ASC")
    override fun searchBasicInfoDateAscending(value: String): Flow<List<BasicInfo>>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value ORDER BY id DESC")
    override fun searchBasicInfoDateDescending(value: String): Flow<List<BasicInfo>>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value ORDER BY rate ASC")
    override fun searchBasicInfoRateAscending(value: String): Flow<List<BasicInfo>>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title LIKE :value OR summary LIKE :value OR review LIKE :value ORDER BY rate DESC")
    override fun searchBasicInfoRateDescending(value: String): Flow<List<BasicInfo>>

    @Query("SELECT id, title, image, rate, `like` FROM `${Constants.TABLE_MOVIE_LIST}` WHERE `like`=1")
    override fun fetchLike(): Flow<List<BasicInfo>>

    @Query("UPDATE `${Constants.TABLE_MOVIE_LIST}` SET `like` = :like WHERE id = :id")
    override suspend fun like(id: Int, like: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM `${Constants.TABLE_MOVIE_LIST}` WHERE title = :title AND image = :image)")
    override suspend fun checkExist(title: String, image: String): Boolean

}