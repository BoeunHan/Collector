package com.han.collector.model.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities=[MovieEntity::class, BookEntity::class, PlaceEntity::class], version=8)
abstract class ReviewDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    abstract fun bookDao(): BookDao

    abstract fun placeDao(): PlaceDao

}