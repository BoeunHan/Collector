package com.han.collector.model.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.han.collector.utils.Converters

@Database(entities=[MovieEntity::class, BookEntity::class, PlaceEntity::class], version=9, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ReviewDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    abstract fun bookDao(): BookDao

    abstract fun placeDao(): PlaceDao

}