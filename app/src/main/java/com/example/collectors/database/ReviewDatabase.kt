package com.example.collectors.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities=[MovieEntity::class], version=6)
abstract class ReviewDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object{
        @Volatile
        private var INSTANCE: ReviewDatabase? = null

        fun getInstance(context: Context): ReviewDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                            ReviewDatabase::class.java,
                        "movie_database"
                    ).fallbackToDestructiveMigration().build()
                }
                INSTANCE = instance
                return instance
            }
        }
    }
}