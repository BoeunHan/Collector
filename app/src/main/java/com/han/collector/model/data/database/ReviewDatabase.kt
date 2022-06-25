package com.han.collector.model.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities=[MovieEntity::class, BookEntity::class], version=6)
abstract class ReviewDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    abstract fun bookDao(): BookDao

}