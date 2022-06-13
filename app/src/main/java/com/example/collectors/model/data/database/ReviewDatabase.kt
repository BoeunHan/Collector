package com.example.collectors.model.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dagger.Provides

@Database(entities=[MovieEntity::class], version=6)
abstract class ReviewDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao


}